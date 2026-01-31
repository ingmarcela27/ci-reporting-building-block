# 🧱 Building Block CI/CD: Publicación de Reportes (GitHub Pages) + Notificación (Google Chat)

Este repositorio contiene un ejemplo funcional de pipeline en **GitHub Actions** para:
- Ejecutar pruebas (Selenium + Cucumber)
- Generar reporte HTML con **Allure**
- Publicarlo automáticamente en **GitHub Pages** (rama `gh-pages`)
- Enviar notificación con métricas a **Google Chat Space** usando Webhook

✅ **Lo más importante:** aunque este repo usa Java/Selenium/Gradle, la idea es reutilizable.  
Si tu equipo **ya tiene su job de tests**, lo que más te sirve de aquí es el **JOB 3: PUBLISH** (publicación + notificación).

---

## 🎯 Qué problema resuelve

En equipos ágiles, los reportes suelen quedar “perdidos” en artifacts o logs.  
Este building block estandariza:

- **Visibilidad:** reporte disponible como sitio web (Pages)
- **Trazabilidad:** histórico de Allure (history) entre ejecuciones
- **Comunicación:** mensaje automático al Google Chat Space con link + métricas

---

## 🧩 Contrato de integración (lo mínimo que tu pipeline debe entregar)

Este building block necesita **un reporte HTML estático** para publicar.

Tienes 2 opciones:

### ✅ Opción A (recomendada): ya tienes el HTML
Tu pipeline ya genera HTML (Allure, Playwright, Cypress, etc.).  
➡️ Solo conectas el **job de publish** para publicar + notificar.

### 🧪 Opción B: tienes resultados crudos
Tu pipeline genera resultados (ej: `allure-results`) y luego un job genera el HTML.  
➡️ Puedes reutilizar el **job report** de este repo o adaptar el tuyo.

> Regla universal: **si no hay HTML generado → no hay publicación en Pages.**

---

## 🧱 Arquitectura del pipeline (alto nivel)

```text
Push / PR / Manual
        ↓
     TEST JOB
        ↓
    REPORT JOB
        ↓
   PUBLISH JOB
      ↙     ↘
GitHub Pages  Google Chat
```
---

## 📁 Archivo clave en este repo

- **Workflow:** `.github/workflows/ci.yml`

Este workflow está dividido en **3 jobs**:

1. **test:** ejecuta pruebas y sube `allure-results` como *artifact*
2. **report:** genera HTML de Allure, restaura *history* y extrae métricas
3. **publish:** notifica a Google Chat + publica a GitHub Pages (`gh-pages`)

---

## ✅ Requisitos del repositorio (muy importantes)

### 1) Permisos del workflow para crear/actualizar `gh-pages`

En el YAML ya está:

```yaml
permissions:
  contents: write
```

## ✅ Permisos de escritura para GitHub Actions (obligatorio)

Además del workflow, en el repo debes habilitar permisos de escritura para Actions:

**Ruta:** Repo → **Settings** → **Actions** → **General** → **Workflow permissions**

- ✅ **Read and write permissions**
- (Opcional) **Allow GitHub Actions to create and approve pull requests**

> Sin esto, el workflow no puede hacer push a `gh-pages`.

---

## 🌐 Configurar GitHub Pages (para publicar el reporte)

1. Repo → **Settings**
2. Menú → **Pages**
3. En **Build and deployment**:
    - **Source:** Deploy from a branch
    - **Branch:** `gh-pages`
    - **Folder:** `/ (root)`
4. **Save**

⚠️ **Nota:** la rama `gh-pages` aparece solo después de que el workflow publique por primera vez (en `main`).

---

## 🔐 Secrets requeridos

### 1) `GITHUB_TOKEN`
- No se crea. GitHub lo inyecta automáticamente.
- Solo asegúrate de tener:
    - `permissions: contents: write` en el workflow
    - **Read and write permissions** activado en el repo

---

### 2) `GCHAT_WEBHOOK_URL` (Google Chat)
Se usa para enviar la notificación al Space.

**Crear webhook**
1. Google Chat → entrar al **Space**
2. Opciones del Space → **Aplicaciones e integraciones**
3. **Añadir webhooks**
4. Copiar la URL

**Guardar en GitHub**
1. Repo → **Settings** → **Secrets and variables** → **Actions**
2. **New repository secret**
3. **Name:** `GCHAT_WEBHOOK_URL`
4. **Value:** (pegar URL)

✅ Si no configuras este secret, el workflow no falla; solo salta la notificación.
---
## ▶️ Cómo ejecutar

El workflow se ejecuta en:

- Push a `main` o `develop`
- Pull Request
- Manual (`workflow_dispatch`)

📌 **Importante:** la publicación a Pages solo ocurre en `main` (por seguridad y limpieza):

```yaml
if: github.ref == 'refs/heads/main'
```

----

## 📌 Dónde ver el reporte publicado

Cuando el job **publish** corre en `main`, el reporte queda en:

`https://<owner>.github.io/<repo>/allureReport/`

> `allureReport` viene de `destination_dir: allureReport`.
> 
---

## 🔁 Cómo reutilizar este building block en tu proyecto (paso a paso)

### Opción 1: Copiar el job `publish` (recomendado si ya generas HTML)

1. Copia el job `publish` de `.github/workflows/ci.yml`.
2. Asegúrate de que tu pipeline tenga el HTML en una carpeta (tu `REPORT_DIR`).
3. Ajusta en tu workflow:
    - `REPORT_DIR` (ruta real del HTML en tu proyecto)
    - `destination_dir` (cómo quieres que aparezca en Pages)
4. Configura:
    - GitHub Pages
    - Permisos del repo (Actions)
    - Secret `GCHAT_WEBHOOK_URL`

✅ Con eso ya publicas y notificas.


### Opción 2: Copiar `report` + `publish` (si tienes `allure-results` pero no HTML)

Si ya generas `allure-results`, puedes copiar también el job `report` para generar el HTML.

**Valida:**
- Que tu ruta de resultados sea correcta (`RESULTS_DIR`)
- Que el comando para generar el reporte exista (en este repo es `./gradlew allureReport`)

---

## 🛠️ Variables importantes (para adaptar rápido)

En este repo:

```yaml
env:
  RESULTS_DIR: build/allure-results
  REPORT_DIR: build/reports/allure-report/allureReport
```
**Qué significa:**

- `RESULTS_DIR`: carpeta donde quedan los resultados crudos (Allure)
- `REPORT_DIR`: carpeta donde queda el HTML final listo para publicar

---

## 🧪 Qué hace especial este workflow

- ✅ Restaura **Allure history** desde `gh-pages` para mantener tendencias
- ✅ Extrae métricas desde `summary.json` y las envía a Google Chat
- ✅ No falla si el webhook no existe (comportamiento seguro)
- ✅ Publica en GitHub Pages usando `peaceiris/actions-gh-pages@v4` y `keep_files: true`

---

## ✅ Checklist de validación (cuando lo implementes en tu repo)

- [ ] El workflow tiene `permissions: contents: write`
- [ ] Repo **Settings → Actions → General** → **Read and write permissions** ✅
- [ ] En `main` se ejecuta el job `publish` (no skipped)
- [ ] Existe `REPORT_DIR` con HTML (si no, no publica)
- [ ] Rama `gh-pages` aparece en **Code → Branches**
- [ ] **Settings → Pages** apunta a `gh-pages`
- [ ] (Opcional) `GCHAT_WEBHOOK_URL` creado y guardado como secret
- [ ] El link del reporte abre correctamente en el browser

---

## 📌 Sugerencia de uso corporativo (para equipos)

Este repo puede servir como:

- **Referencia técnica:** copy/paste del job `publish`
- **Plantilla base:** estandarizar publicación y comunicación de resultados
- **Base para escalar:** convertirlo en un workflow reusable (`workflow_call`) para que los equipos solo lo “llamen”


---

## 📁 Repo de referencia

- **Repositorio:** `ingmarcela27/ci-reporting-building-block`
- **Workflow:** `.github/workflows/ci.yml`
- **GitHub Pages (reporte):** `https://ingmarcela27.github.io/ci-reporting-building-block/allureReport/`
> **Tip:** Si tu pipeline ya genera un HTML, copia solo el job **PUBLISH** y ajusta `REPORT_DIR`.


---



