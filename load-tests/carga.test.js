import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

const errorRate       = new Rate('errores_http');
const tiempoLogin     = new Trend('tiempo_login_ms', true);
const tiempoHealth    = new Trend('tiempo_health_ms', true);
const tiempoProductos = new Trend('tiempo_productos_ms', true);

export const options = {
  stages: [
    { duration: '30s', target: 20 },
    { duration: '1m',  target: 20 },
    { duration: '20s', target: 50 },
    { duration: '30s', target: 50 },
    { duration: '20s', target: 0  },
  ],
  thresholds: {
    http_req_duration:   ['p(95)<500'],
    errores_http:        ['rate<0.01'],
    tiempo_login_ms:     ['p(95)<600'],
    tiempo_health_ms:    ['p(95)<200'],
    tiempo_productos_ms: ['p(95)<500'],
    checks:              ['rate>0.95'],
  },
};

const CREDENCIALES_INVALIDAS = JSON.stringify({
  correo: 'carga_test@test.com',
  password: 'invalid',
});

const HEADERS_JSON = {
  headers: { 'Content-Type': 'application/json' },
};

export default function () {

  group('G1 · Health Check', function () {
    const inicio = Date.now();
    const res = http.get(`${BASE_URL}/health`);
    tiempoHealth.add(Date.now() - inicio);

    const ok = check(res, {
      'health → 200 OK':         (r) => r.status === 200,
      'health → body "OK"':      (r) => r.body && r.body.includes('OK'),
      'health → tiempo < 200ms': (r) => r.timings.duration < 200,
    });
    errorRate.add(!ok);
    sleep(0.3);
  });

  group('G2 · Catálogo de Productos', function () {
    const inicio = Date.now();
    const res = http.get(`${BASE_URL}/api/v1/productos`, HEADERS_JSON);
    tiempoProductos.add(Date.now() - inicio);

    const ok = check(res, {
      'productos → no retorna 500':      (r) => r.status !== 500,
      'productos → responde en < 500ms': (r) => r.timings.duration < 500,
    });
    errorRate.add(!ok);
    sleep(0.5);
  });

  group('G3 · Autenticación bajo carga', function () {
    const inicio = Date.now();
    const res = http.post(`${BASE_URL}/api/v1/auth/login`,
      CREDENCIALES_INVALIDAS, HEADERS_JSON);
    tiempoLogin.add(Date.now() - inicio);

    check(res, {
      'login inválido → no 500':  (r) => r.status !== 500,
      'login inválido → < 600ms': (r) => r.timings.duration < 600,
    });
    errorRate.add(res.status >= 500);
    sleep(0.3);
  });

  group('G4 · Control de acceso bajo carga', function () {
    const res = http.get(`${BASE_URL}/api/v1/usuarios`, HEADERS_JSON);

    check(res, {
      'usuarios sin token → 401 o 403': (r) => r.status === 401 || r.status === 403,
      'rechazo sin token < 300ms':       (r) => r.timings.duration < 300,
    });
    errorRate.add(res.status >= 500);
    sleep(0.2);
  });

  sleep(Math.random() * 1 + 0.5);
}

export function handleSummary(data) {
  const p95  = data.metrics.http_req_duration?.values?.['p(95)']?.toFixed(1) ?? 'N/A';
  const tasa = ((data.metrics.errores_http?.values?.rate ?? 0) * 100).toFixed(2);
  const ok   = ((data.metrics.checks?.values?.rate ?? 0) * 100).toFixed(1);
  const rps  = data.metrics.http_reqs?.values?.rate?.toFixed(2) ?? 'N/A';
  const pasa = parseFloat(p95) < 500 && parseFloat(tasa) < 1 && parseFloat(ok) > 95;

  console.log('\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
  console.log('  T-18 · PRUEBA DE CARGA — NoLimits API');
  console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
  console.log(`  p95 tiempo respuesta : ${p95} ms  (umbral < 500ms)`);
  console.log(`  Tasa de error        : ${tasa}%   (umbral < 1%)`);
  console.log(`  Checks exitosos      : ${ok}%   (umbral > 95%)`);
  console.log(`  Requests por segundo : ${rps} RPS`);
  console.log(`  Resultado: ${pasa ? '✅ APROBADO' : '❌ REPROBADO'}`);
  console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n');
  return { stdout: '' };
}