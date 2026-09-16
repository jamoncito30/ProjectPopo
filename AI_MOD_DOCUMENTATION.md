# 💩 PopoCraft (proyecto_intento) - Documentación Central y Reglas para IA

**¡BIENVENIDO/A, AGENTE IA Y DESARROLLADORES HUMANO!**
Este proyecto está diseñado bajo un modelo **MULTI-IA** y de trabajo colaborativo.

> ⚠️ **REGLAS ESTRICTAS Y OBLIGATORIAS PARA CUALQUIER IA QUE INGRESE AL PROYECTO** ⚠️
> 1. **LECTURA OBLIGATORIA:** Antes de sugerir código, compilar o accionar, DEBES leer este archivo de principio a fin para entender el contexto, las versiones, y las tareas activas.
> 2. **NO ROMPER EL ENTORNO:** El proyecto fue arduamente configurado para **Java 21**, **Gradle 8.9** (con el plugin de Fabric-Loom 1.7.4) para Minecraft 1.21.1. ¡NUNCA introduzcas configuraciones o código que dependa de Java 25 u otras versiones incompatibles!
> 3. **DOCUMENTAR TODO:** Cualquier cambio importante (nuevo ítem, bloque, mecánicas, refactorización) DEBE ser registrado en la sección correspondiente de este archivo inmediatamente después de validarse en el juego.
> 4. **CONFIRMAR ACCIONES:** Al terminar tus tareas, debes informar al usuario humano del éxito, actualizar el estatus de las tareas pendientes y documentar cómo dejaste el código.
> 5. **PLAN Y CONTINUIDAD ANTES DE IMPLEMENTAR:** Antes de modificar código o recursos, registrar aquí el pedido, el plan y las verificaciones previstas con estado **EN DESARROLLO**. Actualizar avances y pendientes durante el trabajo para poder retomarlo tras una interrupción. Marcar **TERMINADO** únicamente tras completar y verificar la implementación; distinguir siempre pruebas automáticas de revisión humana pendiente. Esta regla se aplica a todas las sesiones futuras.

---

## 📖 1. Visión y Concepto del Proyecto
El objetivo principal de **PopoCraft** es introducir mecánicas de juego irreverentes, divertidas y únicas centradas alrededor del ítem base de la "caca" (`popo`). Se buscará expandir sistemas agrícolas, de combate, y utilitarios partiendo de mecánicas biológicas absurdas pero que encajen dentro de las mecánicas de supervivencia de Minecraft.

**Datos Técnicos del Entorno:**
* **Juego:** Minecraft 1.21.1
* **API / Modloader:** Fabric
* **Versión de Java Obligatoria:** Java 21
* **Versión de Gradle:** 8.9

---

## 🛠️ 2. Progreso Actual e Implementaciones (Lo que ya está hecho)
- [x] **Configuración Base del IDE y Compilador:** Solucionado el conflicto masivo de "Java 25" y estabilizados los compiladores, configs de `mixins.json` y `workspace.xml`. Gradle sincroniza limpio.
- [x] **Registro del Mod:** Mod inicializado con el ID `proyecto_intento`.
- [x] **Primer Ítem Registrado (`POPO`):** Ítem básico registrado exitosamente en `ModItems.java`. Cuenta con textura visual implementada.
- [x] **Integración In-Game:** El ítem aparece correctamente asignado a la pestaña "Ingredientes" del Modo Creativo.
- [x] **Expansión inicial 2026-09-14:** producción periódica, alimentación al 25%, bloques húmedo/seco y armadura repelente implementados. Siete pruebas iniciales aprobadas. El usuario confirmó en la sesión siguiente que la armadura equipada se ve bien. Detalles históricos en sección 5; ampliación actual en sección 6.

---

## 📋 3. Tareas en Proceso (IN PROGRESS)
* **Sección 11, tareas ChatGPT — TERMINADAS según reparto:** inodoro/asiento, desatascador, fertilizante gradual, arte del extractor y comerciante. Revisión correctiva de integración: 38/38 GameTests, cliente y build aprobados; entrega y pendientes de Antigravity en sección 12 y `art/ROADMAP_HANDOFF.md`.
* **Gran refugio comunitario — MODELOS TERMINADOS / LÓGICA PENDIENTE:** cuatro etapas visuales verificadas en Minecraft, generador y guía de integración listos. Especificación y continuidad en sección 10.
* **Remake de popó, efecto, alquimia y sonido — TERMINADO:** 26/26 GameTests, build y cliente aprobados; entrega en sección 9. Queda apreciación humana del sonido y seguimiento de la intermitencia observada en la prueba antigua de construcción de escarabajos.
* **Refugios y colonias de escarabajos — TERMINADO:** 21/21 GameTests, build y comprobación del cliente aprobados. Captura de las cinco fases inspeccionada. Pendiente únicamente apreciación humana del comportamiento y aspecto en una partida normal. Entrega y parámetros en sección 8.
* **Segunda sesión 2026-09-14 — Codex:** desarrollo terminado, build y **13/13 GameTests aprobados**, comprobación automática de recursos en cliente aprobada. Pendiente apreciación humana de los iconos dentro del inventario, animación del escarabajo, densidad de moscas y volumen de los sonidos. La carga automática de recursos no sustituye observar y escuchar una partida.

---

## 🚀 4. Ideas y Funcionalidades a Futuro (Ideas Backlog)
Cualquier IA que retome el proyecto, puede consultar esta lista y preguntar al usuario cuál de estas funcionalidades (o una nueva) quiere implementar a continuación:

### 🟢 Nivel 1: Jugabilidad Central del Popó
- **Función Fertilizante:** Modificar el Popó para que actúe igual que el "Polvo de Hueso" (Bone Meal) para cultivar semillas al darle clic derecho.
- **Arma Arrojadiza:** implementada en sección 6: clic derecho lanza popó y aplica cinco segundos de peste/pánico a mobs. No causa daño directo.

### 🟡 Nivel 2: Crafteos y Bloques Físicos
- **Bloque de Popó:** implementado como bloque húmedo con hundimiento y variante seca por horno (sección 5). Moscas ambientales añadidas en sección 6.
- **Biomasa (Combustible):** Hacer que el Popó sea quemable en los hornos, ofreciendo una fuente de combustible renovable de bajo poder.

### 🔴 Nivel 3: Funciones Avanzadas del Mundo
- **Drop Natural (Mecánica Biológica):** implementado para `AnimalEntity`, con temporizador persistente y alimentación (sección 5).
- **Armas Apestosas:** pendiente crear herramientas que apliquen veneno. La armadura repelente ya está implementada; no aplica veneno.

---
*(Las secciones siguientes contienen el historial y las verificaciones más recientes. Leer hasta el final.)*

## 5. Sesión 2026-09-14 — Codex: expansión de PopoCraft

**Estado final de la sesión:** código y recursos implementados; build exitoso con Java 21 y Gradle 8.9. **7/7 pruebas GameTest aprobadas dentro de Minecraft 1.21.1**. PNG finales inspeccionados. Pendiente revisión visual de la armadura equipada y experiencia audiovisual en cliente; el conector de escritorio devolvió `Computer Use native pipe is unavailable`.

### Cambios implementados
- `AnimalEntityMixin` / `PopoDrops`: animales adultos producen 1 popó cada 6000–12000 ticks (5–10 minutos a 20 TPS), únicamente en servidor y con IA activa. Temporizador persistido en NBT `PopoCraftDropTicks`; se pausa cuando el animal no está cargado. `AnimalEntity.eat` añade 25% por comida aceptada, incluyendo crías y creativo; un clic rechazado no activa la probabilidad. `HorseFeedingMixin` cubre alimentación aceptada por caballos y sus variantes; `WolfFeedingMixin` cubre comida que cura lobos, sin duplicar la tirada de reproducción. No se considera alimentación teñir collares, equipar armadura o usar huesos para domesticar.
- `ModBlocks` / `WetPopoBlock`: `wet_popo`, receta 3×3 con 9 popós. Bloque colocable sin colisión sólida, fricción horizontal y hundimiento lento para jugadores, reinicio de daño de caída; náusea y fatiga minera II en supervivencia. Las botas de cuero no permiten caminar encima. `LivingEntityMixin` reutiliza la lógica vanilla de aire/ahogamiento cuando el bloque cubre los ojos: respeta Respiración, respiración acuática y creativo. No genera congelamiento ni daño de asfixia de bloque sólido.
- `dry_popo`: un bloque húmedo al horno, 200 ticks (10 segundos), 0.1 XP. Bloque sólido decorativo, herramienta eficiente: pico. Bloque húmedo: pala. Ambos devuelven su propio bloque al romperse.
- `ModArmor`: casco, pechera, grebas y botas hechos con popó en los patrones vanilla. Protección 1/3/2/1 y durabilidad 110/160/150/130. Reparación con bloque seco. Conjunto completo requerido, radio 12 bloques, huida a velocidad 1.65 y sonido propio más agudo cada 2–4 segundos. `MobEntityMixin` sustituye temporalmente decisiones de IA al huir para cubrir también mobs con Brain (aldeanos/piglins); se reanuda la IA al quitarse una pieza o alejarse. Wither y dragón excluidos por sus sistemas de combate especiales. Mobs encerrados pueden no encontrar salida.
- Recursos: modelos, estados de bloque, loot tables, recetas y desbloqueo del recetario, etiquetas de herramientas y traducciones es_es/es_cl/en_us. Directorios de datos en singular como requiere 1.21.1.
- Arte: originales ImageGen en `art/source/`; bloques e iconos finales 16×16 y capas de armadura 64×32. `tools/build-assets.ps1` empaqueta las texturas y genera los JSON de forma reproducible. Conservado el `popo.png` original creado con Gemini.
- `SlimeMoveControlAccessor`: adapta la huida al movimiento por saltos de slimes y cubos de magma. `fabric.mod.json`: declara Java >=21 y corrige el icono para apuntar al PNG existente.
- `build.gradle`: añade únicamente configuración optativa `-Pgametest`, con código de pruebas en `src/gametest` y mundo aislado en `build/gametest`. Las pruebas no forman parte del JAR distribuible ni cambian el mundo de desarrollo en `run/saves`. No se cambiaron versiones, JDK del IDE ni configuración global.

### Registro de verificaciones
- Compilación y empaquetado: **OK**, Java 21.0.12, Gradle 8.9, Loom 1.7.4; sin actualizar versiones.
- Pruebas dentro del motor: **7/7 OK**, informe `build/gametest-results.xml`, salida `build/gametest/logs/latest.log`. Incluyen temporizador/guardado/crías, 400 alimentaciones aceptadas y clics rechazados, carga de las seis recetas y fundición real de 200 ticks, mareo/fatiga/aire/daño/recuperación, alimentación especial de caballos y lobos, desplazamiento real de hundimiento durante 60 ticks y huida de vaca/husk/aldeano/slime. La prueba comprueba también que una pieza ausente desactiva la detección del conjunto.
- Arte: `art/texture-preview.png` revisado, sin texturas faltantes en los archivos generados. Revisión visual en cliente: **pendiente**. Los sonidos se reutilizan del juego a tono elevado; no son grabaciones de gritos nuevas.
- `genSources --offline` no disponible porque Vineflower 1.10.1 no estaba en caché; no afecta la compilación. Se consultaron las firmas y bytecode del Minecraft local y la documentación oficial Yarn 1.21.1.

### Reproducir y probar manualmente

Usar **Java 21 y Gradle 8.9**, ya instalados. En esta máquina:

```powershell
$env:JAVA_HOME = 'C:/Users/jorge/.jdks/ms-21.0.12.1'
$gradleExe = 'C:/Users/jorge/.gradle/wrapper/dists/gradle-8.9-bin/90cnw93cvbtalezasaz0blq0a/gradle-8.9/bin/gradle.bat'
& $gradleExe --offline build
& $gradleExe --offline -Pgametest runGametest
# Solo si se modifican los originales o las definiciones del generador:
powershell -NoProfile -ExecutionPolicy Bypass -File tools/build-assets.ps1
```

- Entregable: `build/libs/proyecto_intento-1.0-SNAPSHOT.jar` (no usar el `-sources.jar` para jugar).
- En el cliente, los bloques están en Construcción y la armadura en Combate. Comandos útiles: `/give @s proyecto_intento:popo 64`, `/give @s proyecto_intento:wet_popo 64`, `/give @s proyecto_intento:dry_popo 64` y `/give @s proyecto_intento:popo_helmet` (análogos `popo_chestplate`, `popo_leggings`, `popo_boots`).
- Colocar un bloque húmedo sobre suelo sólido y pisarlo en supervivencia: hundimiento gradual, mareo y minería más lenta. Probar una columna de dos o tres bloques: el aire baja cuando cubre los ojos. Salir lateralmente o romper el bloque; los efectos duran brevemente tras salir (náusea hasta 5 s, fatiga hasta 2 s).
- Equipar las cuatro piezas cerca de animales/hostiles/aldeanos en terreno abierto: comprobar huida, sonido y retorno de comportamiento normal al quitar una pieza. Una barrera física puede impedir la huida. La armadura tiene protección modesta; no concede inmunidad a proyectiles ya lanzados ni a daño por contacto.
- Alcance biológico: producción en clases `AnimalEntity` (animales), no en monstruos, aldeanos ni entidades inanimadas. Eventos especiales de domesticación o comidas de otros mods que omitan las rutas vanilla cubiertas no están garantizados. Jefes Wither/dragón conservan su IA.
- Prompts completos y procedencia del arte: `art/imagegen-prompts.json` (herramienta integrada ImageGen, sin API/CLI externa).

### Referencias técnicas consultadas
- [AnimalEntity — Yarn 1.21.1 build 3](https://maven.fabricmc.net/docs/yarn-1.21.1%2Bbuild.3/net/minecraft/entity/passive/AnimalEntity.html)
- [ArmorMaterial — Yarn 1.21.1 build 3](https://maven.fabricmc.net/docs/yarn-1.21.1%2Bbuild.3/net/minecraft/item/ArmorMaterial.html)
- [Cambios Fabric para Minecraft 1.21 y 1.21.1](https://fabricmc.net/2024/05/31/121.html)

## 6. Segunda sesión 2026-09-14 — lavado, proyectil, moscas y escarabajo

Esta sección reemplaza los parámetros anteriores donde difieran. El usuario aprobó la apariencia de la armadura equipada y pidió corregir los iconos, crear una pestaña propia y añadir estas mecánicas. Se mantuvieron Java 21, Gradle 8.9, Loom 1.7.4 y Minecraft 1.21.1.

### Arte e inventario creativo
- `ModItemGroups`: pestaña **PopoCraft**, con popó, ambos bloques, montón, cuatro piezas de armadura y huevo de escarabajo (9 entradas). Se retiraron las inserciones del mod en las pestañas vanilla; los elementos siguen disponibles en la búsqueda creativa.
- Iconos: casco frontal nuevo y simétrico; pechera/grebas revisadas, y los cuatro iconos centrados mediante sus límites visibles. Las botas mantienen su diseño. PNG finales 16×16 en `assets/proyecto_intento/textures/item/`.
- Las capas equipadas `popo_layer_1.png` y `popo_layer_2.png` permanecen idénticas (SHA-256 `FB7B83599BA776529BB9E6FE4EE85E3DFA87E3FCA5491AC302C7C1EEE761AA49` y `F9E0648C0F05272C4424F923EC19342DA183B3927FC3C966C8B0F18B84167A97`).
- ImageGen integrado creó/revisó los originales `armor_icons_v2.png`, `helmet_v2.png`, `beetle_chitin.png` y `fly.png` en `art/source`. Procedencia y prompts: `art/expansion-imagegen-prompts.json`. Vista de los iconos: `art/armor-icons-v2-preview.png`.
- Generación reproducible: `tools/build-assets.ps1` llama a `build-expansion-assets.ps1`, que reutiliza `build-base-assets.ps1`. Usar siempre la entrada pública `build-assets.ps1` para no regenerar solamente la versión anterior. Se conservan todos los originales anteriores.

### Lavado y ambiente de moscas
- `SmellSystem`: tocar agua con el conjunto completo sucio lo limpia inmediatamente en servidor. Mientras permanece mojado se refresca el lavado una vez por segundo. Cada lavado dura aleatoriamente 3600–6000 ticks (aproximadamente 3–5 minutos a 20 TPS).
- El vencimiento se guarda **en cada pieza** mediante `CUSTOM_DATA.PopoCraftCleanUntil`, conservando los demás datos del objeto. Persiste al guardar, cambiar de dimensión, soltar o volver a equipar. Usa tiempo de juego total, no la hora del día; `/time set day` no borra el lavado. El tiempo no avanza si el servidor está apagado.
- Durante el lavado no hay moscas ni repulsión procedentes de la armadura. El conjunto necesita cuatro piezas limpias; mezclar una pieza sucia vuelve a generar olor. Mojar piezas sueltas también las limpia en la actualización de un segundo. El lavado de armadura no elimina el efecto independiente del proyectil sobre mobs.
- `FlyParticle`: partícula propia 8×8, cuerpo oscuro y alas claras, vuelo orbital con oscilación vertical. Sigue a las entidades apestosas; desaparece al cesar su olor. Se genera alrededor de conjuntos completos sucios y mobs afectados por el proyectil.
- `WetPopoBlock.randomDisplayTick`: más moscas sobre caras superiores expuestas al aire. No se generan sobre caras tapadas. No se cambiaron el hundimiento ni el ahogamiento.
- `SmellAmbience`: un único presupuesto de audio por cliente, **un zumbido cada 12–22 segundos**, volumen 0.10 con ganancia adicional 0.6. Campos de bloques húmedos no superponen un sonido por bloque. Cerca de un bloque puede seguir habiendo zumbido aunque la armadura esté limpia.
- `ModSounds` y `sounds.json`: zumbido basado en el sonido vanilla de abeja, chirrido/pasos del escarabajo basados en silverfish y rodado sobre barro; tono/volumen ajustados. Son reutilizaciones de audio vanilla, no nuevas grabaciones OGG. Incluyen subtítulos en español e inglés. Sonido de moscas bajo la categoría Ambiente.

### Proyectil y pánico
- `PopoItem` reemplaza el ítem base manteniendo el mismo ID y textura. Clic derecho lanza una unidad, conserva el objeto en creativo, cooldown de 12 ticks (0.6 segundos).
- `PopoProjectileEntity`: trayectoria con gravedad, render del ítem existente, salpicadura y sonido al colisionar; se elimina al impacto o tras 200 ticks. No devuelve munición ni causa daño directo.
- `ModEffects.STINKY` (`proyecto_intento:stinky`): impacto real en un `MobEntity` añade **100 ticks / 5 segundos**. El afectado es una fuente de olor de radio 12, provocando la huida de sus vecinos. Otro impacto refresca los cinco segundos, sin acumular minutos.
- `MobEntityMixin`: reconoce armadura sucia y mobs apestados como fuentes. El afectado corre por objetivos alrededor de su posición inicial, formando círculos cuando el terreno lo permite. Al terminar el efecto se recupera la IA normal, salvo que siga cerca de otra fuente de olor.
- Sonidos de pánico aumentados de cada 2–4 segundos a **cada 12–24 ticks (0.6–1.2 s)**, volumen 0.65 y tono 1.5–1.85. Se conserva el sonido propio de cada mob y la adaptación de salto para slimes/cubos de magma.
- Se mantienen las excepciones de Wither/dragón y de mobs con IA desactivada. El movimiento respeta navegación; paredes, encierros y terrenos sin ruta pueden impedir los círculos o la huida. El proyectil afecta mobs, no jugadores; no es un sistema de control PvP.

### Escarabajo pelotero y montones
- `DungBeetleEntity`: mob pasivo nuevo, 8 puntos de salud (4 corazones), dimensiones 0.65×0.4 bloques. Aparición natural poco frecuente (peso 5, grupos 1–2) en llanuras, sabanas y bosques, bajo reglas vanilla de aparición de animales. Huevo propio en la pestaña PopoCraft. No fuerza aparición en chunks antiguos ya poblados.
- Recoge exclusivamente entidades de ítem `popo` alcanzables en un radio de 12 bloques (3 verticales). Busca ruta y comprueba visibilidad antes de recoger. Lleva **una unidad por viaje**, descontándola correctamente de stacks más grandes.
- Establece hogar al empezar, restringe deambulación a unos 12 bloques y guarda hogar/carga en NBT (`PopoHome`, `CarriedPopo`). Busca un montón cerca del hogar o un espacio libre con suelo sólido; no rompe ni reemplaza construcciones. Se detiene si no encuentra destino y conserva su carga.
- `PopoPileBlock` (`popo_pile`): montón decorativo con estado `amount=1..8` y altura variable. Cada depósito aumenta el contenido en uno; romperlo devuelve exactamente esa cantidad de popó. Se puede fabricar un montón inicial con una unidad de popó. Herramienta eficiente: pala.
- Recolección y colocación respetan `mobGriefing`. Dos escarabajos revisan el estado del ítem antes de recogerlo para evitar duplicación. Al morir puede soltar la unidad transportada mediante el flujo de loot vanilla. Las crías no recolectan. Pueden alimentarse/criarse con semillas de trigo.
- `DungBeetleModel` / `DungBeetleRenderer`: modelo propio, caparazón partido de quitina oscura/bronce, seis patas con marcha alternada, antenas oscilantes y bola frontal visible/rotatoria solo al transportar. Textura 64×64; material de la bola reutiliza el bloque húmedo. Chirridos suaves, pasos y sonido de rodado.

### Comprobaciones y entrega
- Primera ejecución: **12/12 GameTests aprobados**, incluyendo las siete pruebas previas más lavado/persistencia/vencimiento, impacto real y duración del proyectil, pánico y repulsión entre mobs, recogida/depósito conservando cantidades, y NBT de hogar/carga.
- Comprobación de cliente: **APROBADA**. Cliente Minecraft/OpenGL/OpenAL iniciado en `build/clientcheck`, cargó 9 modelos de ítems, 5 texturas específicas, piezas del modelo del escarabajo y 4 eventos de sonido; se cerró automáticamente. Informe `build/clientcheck/resource-check.txt`.
- Ejecución final: **13/13 GameTests aprobados y BUILD SUCCESSFUL**, incluida competencia de dos escarabajos por un solo ítem. Informe final en `build/gametest-results.xml`. JAR actualizado en `build/libs/proyecto_intento-1.0-SNAPSHOT.jar`.
- El conector de escritorio siguió indisponible (`Computer Use native pipe is unavailable`). No se afirma revisión visual de una partida ni escucha humana de estos sonidos. Los PNG de inventario sí fueron inspeccionados y se validó la carga del cliente.
- Minecraft informa que no hay DataFixer para las dos entidades nuevas: aviso de esquema de migración, sin impedir creación, guardado/carga o las pruebas. No se declara compatibilidad de migración con futuras versiones del juego.
- Código de comprobación aislado en `src/gametest` y `src/clientcheck`, habilitado por propiedades Gradle; se excluye del JAR distribuible. La prueba de cliente no modifica `run/saves` ni la configuración normal del IDE.

Comandos adicionales, usando las mismas variables `$env:JAVA_HOME` y `$gradleExe` de la sección 5:

```powershell
& $gradleExe --offline -Pgametest runGametest build
& $gradleExe --offline -Pclientcheck runClientcheck
powershell -NoProfile -ExecutionPolicy Bypass -File tools/build-assets.ps1
```

Para revisión humana: abrir PopoCraft en creativo; lanzar popó con clic derecho contra una vaca y observar durante cinco segundos; mojar la armadura y comprobar que el olor vuelve entre tres y cinco minutos después; colocar bloques húmedos y escuchar los zumbidos espaciados. Generar dos escarabajos, soltar popó cerca y observar la bola y el montón. Comandos: `/summon proyecto_intento:dung_beetle`, `/give @s proyecto_intento:dung_beetle_spawn_egg`, `/give @s proyecto_intento:popo_pile`.

## 7. Verificación de continuidad — 2026-09-14

- A petición del usuario, se contrastó la última entrega (sección 6) con el código y los recursos actuales. Están presentes el registro de la pestaña, lavado, proyectil, moscas, sonidos, escarabajo y montones. Las dos texturas de armadura equipada mantienen los SHA-256 documentados.
- Se volvió a ejecutar `--offline -Pgametest runGametest build` con Java 21 y Gradle 8.9: **13/13 GameTests aprobados y BUILD SUCCESSFUL**. Informe actualizado: `build/gametest-results.xml`. No fue necesario modificar código funcional.
- Se consultó el informe existente `build/clientcheck/resource-check.txt`, con resultado PASS; la comprobación de cliente no se repitió en esta revisión.
- Sigue pendiente la apreciación humana de iconos, animación del escarabajo, densidad de moscas y volumen de sonidos durante una partida. No se encontró una implementación pendiente indicada por la última sesión.

## 8. Refugios y colonias de escarabajos — TERMINADO (2026-09-14)

### Pedido y plan registrado antes de implementar
- Crear un refugio pequeño de un bloque con **cinco fases visibles**, construido progresivamente mediante popó recolectado. Conservar los montones decorativos existentes para compatibilidad.
- Permitir que adultos y crías entren y salgan del refugio terminado, almacenando sus datos de forma persistente como habitantes. Liberarlos al romper el refugio sin duplicarlos ni perderlos.
- Tras completar la casa, los aportes adicionales de popó servirán de alimento para reproducir una pareja adulta cercana dentro del refugio. Añadir tiempos de descanso, maduración y capacidad limitada para controlar crecimiento; descendientes podrán establecer casas vecinas al crecer y disponer de material.
- Ampliar aparición natural rara a los biomas terrestres del Overworld, sobre terreno adecuado y con reglas de luz/espacio de animales. Interpretación de «cualquier parte»: distribución por el mundo de superficie, sin aparición sumergida, en el vacío, Nether o End. Rareza comparable como intención de diseño, sin prometer frecuencia exacta de tortugas.
- Mantener Java 21, Gradle 8.9, Loom 1.7.4 y Minecraft 1.21.1. Respetar `mobGriefing`, construcciones existentes, guardado y cantidades de ítems.

### Pasos y verificaciones completados
- [x] Bloque de refugio, cinco modelos, traducciones y registro creativo.
- [x] Habitantes persistentes, entrada/salida, reproducción, crías y liberación al romper.
- [x] Recolección y construcción autónoma; expansión a casas próximas.
- [x] Aparición natural rara ampliada.
- [x] GameTests de construcción, reproducción, persistencia, rotura y conservación; regresiones previas y build.
- [x] Comprobación de recursos en cliente; documentar parámetros finales y límites.

**Avance intermedio:** implementados bloque y entidad de bloque, habitantes NBT, reproducción y salida, construcción/recolección y adopción de casa. Primera compilación Java aprobada. Generador de cinco modelos incorporado a `tools/build-assets.ps1`. Pendientes pruebas ampliadas y carga del cliente; no declarar terminado todavía.

**Parámetros de la primera implementación (sujetos a verificación):** cinco aportes forman las cinco etapas; aportes posteriores almacenan hasta ocho alimentos. Capacidad interior seis; una pareja consume un alimento por cría, espera mínima interior de diez segundos, descanso reproductivo de cinco minutos y maduración de veinte minutos. Máximo local de dieciséis escarabajos para nacimientos automáticos. Dos adultos asociados por casa; al crecer, los hijos buscan casa o lugar vecino. Aparición peso 2, grupos 1–2, terreno natural iluminado y expuesto al cielo en biomas del Overworld, con filtro adicional 1/4 en intentos de aparición.

**Checkpoint de pruebas:** primera suite ampliada 16/18 aprobadas. Construcción autónoma y nacimiento confirmados; fallan dos conteos de población tras salir (esperado 3, contado 2 dentro del área GameTest). Investigar límite del área frente a pérdida real; aún no resuelto. Depuración aislada en `build/gametest-debug`, sin usar `run/saves`. Pantalla de QA de cinco etapas agregada en `src/clientcheck`; pendiente ejecutar.

**Diagnóstico confirmado con IntelliJ:** logpoints registraron tres UUID liberados y se consultaron vivos por UUID al fallar el conteo. Un adulto estaba a Y=-60 fuera del suelo/volumen de prueba (Y=-58); `getEntities` contaba solo dos. Corregido el recinto de los nuevos GameTests con paredes perimetrales. Sin pérdida de habitantes observada. Logpoints eliminados y sesión de depuración detenida. Se añaden pruebas de retorno de crías, maduración/expansión, capacidad y registro de aparición en varios biomas.

**Segundo checkpoint:** 20/21 GameTests aprobados, incluidos familia completa, construcción/nacimiento autónomos y casa vecina. El selector `foundInOverworld` solo reconocía el bioma del generador superplano en GameTest (registro: 1 bioma modificado); ampliado con la etiqueta `IS_OVERWORLD` para cubrir todos los biomas de superficie aun en ese tipo de mundo. Añadida marca persistente de residencia infantil para que al madurar el hijo busque casa sin desplazar a los padres. Pendiente última suite y cliente.

**Tercer checkpoint:** **21/21 GameTests aprobados y BUILD SUCCESSFUL**. JAR distribuible inspeccionado: incluye bloque/entidad/modelos nuevos y excluye clases de GameTest y QA del cliente. Primera comprobación de cliente aprobada (10 ítems, 5 fases, texturas y sonidos anteriores). Captura renderizada revisada; ajustando únicamente la cámara de QA para mostrar la entrada frontal. La implementación funcional está verificada.

### Entrega final y comportamiento vigente

**Estado: TERMINADO.** Los checkpoints anteriores son historial, no tareas pendientes. Se añadió la regla obligatoria 5 al comienzo de este documento: registrar siempre el plan antes de implementar y mantener estados/avances para retomar tras interrupciones.

- **Refugio `proyecto_intento:beetle_nest`:** ocupa un bloque. Cinco aportes de una unidad crean sucesivamente base, paredes, cubierta parcial, cubierta y remate/entrada. Cinco modelos de geometría propia reutilizan las texturas húmeda/seca existentes. Una sexta unidad pasa a alimento; almacena hasta ocho. Disponible en PopoCraft (ahora diez entradas); el objeto colocado empieza en fase 1. Los montones decorativos anteriores permanecen, pero la recolección autónoma ahora construye refugios.
- **Construcción y pequeñas colonias:** cada adulto carga una unidad. Dos adultos cercanos pueden adoptar la misma casa y colaborar. Busca casas hasta ocho bloques alrededor del hogar y lugares de construcción hasta seis; deja separación mínima de tres bloques entre posiciones de casas, suelo sólido y una salida lateral libre. No reemplaza bloques existentes. Conserva la carga si no puede depositar. `mobGriefing=false` detiene recolección y construcción/aportes; no expulsa familias ya alojadas.
- **Vida dentro/fuera:** casa terminada con capacidad interior de seis. Al entrar se guarda la entidad completa y se retira del mundo; al salir se restaura su UUID, nombre, edad y datos. Adultos descansan normalmente 20 segundos; adultos listos con comida esperan hasta 60 segundos para dar tiempo a una pareja. Crías descansan 30 segundos. Tras salir pasan aproximadamente un minuto fuera, salvo adultos listos que regresan antes si hay comida. Las crías también buscan el hogar y entran solas; no recolectan.
- **Reproducción:** dos adultos sin descanso reproductivo, cada uno con al menos diez segundos dentro, consumen **un popó** para generar una cría dentro de la casa. Los padres esperan cinco minutos antes de poder reproducirse otra vez. Las crías maduran en veinte minutos; su edad avanza tanto dentro como fuera del refugio mientras esté cargado. No avanza mientras el chunk no está cargado o el servidor está apagado. Se conserva la reproducción manual con semillas de trigo anterior.
- **Expansión y límites:** los hijos conservan `NurseryResident` hasta madurar; entonces buscan su propio lugar sin desplazar a los padres. Con material disponible pueden iniciar casas vecinas. Los nacimientos automáticos se detienen con dieciséis escarabajos locales (visibles en un radio de 16 y alojados en refugios cercanos, dentro de dos bloques verticales). Es un límite local, no un límite mundial ni una garantía de que nazca una colonia sin alimento, pareja y espacio.
- **Persistencia y rotura:** NBT del refugio `Food`/`Occupants`, por habitante `Entity`/`TicksInside`; NBT del escarabajo conserva `PopoHome`/`CarriedPopo` y añade `BeetleNest`/`OutsideTicks`/`NurseryResident`. Salidas bloqueadas retienen habitantes; romper el refugio los evacua incluso si está rodeado. En supervivencia devuelve un popó por fase construida más el alimento sin consumir. El alimento gastado en nacimientos no se devuelve. Identidades duplicadas cargadas se rechazan al liberar.
- **Aparición natural rara:** registro en biomas con etiqueta `IS_OVERWORLD` y biomas usados por el generador de superficie. Peso relativo 2, grupos 1–2 y filtro aleatorio adicional 1/4 en la restricción de aparición. Requiere cielo visible, luz superior a 8 y terreno natural: tierra/hierba, arena, piedra, grava, nieve sólida, musgo o terracota. Respeta espacio, distancia al jugador y cupos vanilla. Puede aparecer en zonas apropiadas de desiertos, playas, selvas, nieve y otros biomas; no bajo el agua, ni en Nether/End. La frecuencia exacta depende del mundo y no es idéntica a la de tortugas. No fuerza aparición instantánea ni repuebla automáticamente chunks viejos.

### Evidencia final

- `--offline -Pgametest runGametest build`: **21/21 aprobados**, `BUILD SUCCESSFUL`, Java 21 / Gradle 8.9 / Loom 1.7.4 / Minecraft 1.21.1. Informe: `build/gametest-results.xml`; ejecución: `build/nest-test-run.log`.
- Incluye las trece pruebas anteriores (adaptadas a depósitos en refugios), cinco fases/cantidades, familia y NBT, bloqueo de salidas/rotura, construcción y nacimiento autónomos, ausencia de pareja, ausencia de comida/capacidad, entrada y salida de crías/fundación de casa vecina, y aparición registrada en varios biomas de superficie.
- `--offline -Pclientcheck runClientcheck`: **APROBADO**, diez modelos de ítems, cinco fases del refugio, cinco texturas específicas, modelo del escarabajo y cuatro eventos de sonido. Informe: `build/clientcheck/resource-check.txt`.
- Captura con los modelos realmente renderizados por Minecraft: `build/clientcheck/screenshots/refugios.png`, copiada a `art/beetle-nest-stages.png` e inspeccionada. Esto verifica geometría/texturas; queda apreciación humana de una partida normal y del ritmo de la colonia.
- JAR final: `build/libs/proyecto_intento-1.0-SNAPSHOT.jar`. Se verificó que incluye refugios y recursos y excluye las clases de pruebas y la pantalla QA. No se cambiaron versiones ni mundos de `run/saves`.
- Generador nuevo `tools/build-nest-assets.ps1`, invocado desde la entrada pública `tools/build-assets.ps1`. Reutiliza las texturas PNG existentes y no depende de una herramienta externa de generación de imágenes.

### Prueba manual rápida

1. En creativo, generar dos escarabajos cercanos sobre suelo plano usando huevos de la pestaña PopoCraft o `/summon proyecto_intento:dung_beetle`.
2. Obtener `/give @s proyecto_intento:popo 16` y **soltar ítems con Q**, no lanzarlos con clic derecho. Cinco unidades construyen la casa y una adicional puede alimentar un nacimiento. Observar la bola transportada y las cinco fases.
3. Dejar que ambos entren: tras al menos diez segundos juntos y con comida aparecerán corazones. Los padres y la cría saldrán según sus tiempos de estancia. Proporcionar más popó y espacio para futuras casas cuando crezcan las crías.
4. Para probar directamente una casa completa: `/setblock ~2 ~ ~ proyecto_intento:beetle_nest[stage=5]` sobre suelo libre. Después soltar alimento y acercar dos adultos.
5. Romper una casa ocupada y comprobar que salen sus habitantes. Cerrar/reabrir el mundo para comprobar continuidad. La aparición natural se aprecia mejor explorando terreno nuevo; los huevos permiten probar la mecánica sin esperar una aparición rara.

Referencias de API: [BlockEntity Yarn 1.21.1](https://maven.fabricmc.net/docs/yarn-1.21.1%2Bbuild.3/net/minecraft/block/entity/BlockEntity.html), [BiomeSelectors Fabric](https://maven.fabricmc.net/docs/fabric-api-0.100.1%2B1.21/net/fabricmc/fabric/api/biome/v1/BiomeSelectors.html). Se verificaron también las firmas del Minecraft local; no se actualizó ninguna dependencia.

## 9. Remake, efecto de peste y Pedo en botella — TERMINADO (2026-09-15)

### Pedido y plan previo
- Rehacer la textura del ítem popó con ImageGen, adaptada al pixel art del mod. Conservar el original como respaldo y mantener ID/recetas. Actualizar también el icono del efecto que reutiliza esta textura.
- Verificar el efecto registrado `proyecto_intento:stinky` mediante `/effect`, su autocompletado y aplicación a jugadores/mobs; corregir cualquier carencia encontrada y documentar el comando exacto.
- Registrar la poción **Pedo en botella**, elaborable en el soporte de pociones con popó. Usar poción rara como base, duración inicial de 30 segundos, y conversiones vanilla a arrojadiza/persistente. Incluirla en PopoCraft y traducir sus variantes. La peste adquirida por un jugador hará que los mobs huyan de él sin controlar sus movimientos.
- Añadir sonido de pedo a cada producción real de popó (periódica o por comida), sustituyendo el sonido de barro. Revisar como referencia https://www.youtube.com/watch?v=cl0SVX78XM4 ; producir un efecto original corto apropiado para el juego si no es accesible, indicando la procedencia real. No afirmar que se escuchó el video si no se puede reproducir.
- Sugerir una idea adicional al entregar, sin implementar funcionalidades no solicitadas.

### Verificaciones previstas
- [x] Original respaldado, textura nueva empaquetada y preview inspeccionado.
- [x] Efecto comprobado por comando/autocompletado y sobre jugador.
- [x] Alquimia real y consumo de poción; variantes y nombres en cliente.
- [x] Audio propio OGG, evento registrado y producción real enlazada.
- [x] Regresiones GameTest, build Java 21 y comprobación del cliente.

**Checkpoint de reanudación 2026-09-15:** textura ImageGen guardada en `art/source/popo_v2.png`, original respaldado en `art/source/popo_original.png`, textura 16×16 y efecto actualizados con `build-alchemy-assets.ps1`; preview inspeccionado en `art/popo-v2-preview.png`. `ModPotions` registra poción rara + popó, 600 ticks, y tres variantes creativas. `PopoDrops` usa `ModSounds.FART` una vez por producción real. Tres OGG originales generados con `tools/build-fart-audio.py` (0.48/0.64/0.79 s, mono 44100 Hz); dependencias locales en `build/audio-deps`, la ejecución requiere acceso fuera del sandbox a esos paquetes. Video no accesible y no escuchado.

**Pruebas: 24/26 aprobadas en la primera ejecución.** Ya pasan autocompletado/aplicación/retirada por `/effect`, bebida y botella vacía, alquimia real de tres botellas y conversiones vanilla, y un pedo por producción. Pendiente resolver nube persistente (no afectó a la vaca en la colocación de prueba) y una regresión intermitente en construcción autónoma de escarabajos; después ejecutar cliente, comprobar nombres/audio/textura y empaquetar. No marcar terminado todavía.

**Avance de reanudación:** audio OGG generado y decodificado, sin saturación (picos 0.71–0.74), procedencia en `art/fart-audio-provenance.json`. Cliente QA ampliado para nombres/modelos de las tres pociones, decodificación Minecraft de tres OGG y captura del icono/pociones. Depurador confirmó en la repetición `complete=1`, `population=3`, `babies=1` para escarabajos, sin modificar su implementación. Investigando nube persistente antes de la suite final.

**Nube diagnosticada:** impacto directo sobre la cabeza creó la nube vanilla a Y=-56..-55.5, mientras la vaca ocupaba Y=-58..-56.6 (cajas comprobadas en IntelliJ). No se intersectaban. Prueba ajustada para impactar el suelo junto a la vaca, conservando comportamiento vanilla. Sesión detenida y breakpoints del agente eliminados. Pendiente suite final y cliente.

**Checkpoint cliente:** `runClientcheck` aprobado. Comprueba diez ítems, tres variantes de poción con nombre traducido, cinco fases de refugio, cinco texturas, cinco eventos sonoros y decodifica los tres OGG con el lector real de Minecraft. Captura `art/alchemy-preview.png` inspeccionada. La suite de 26 pruebas pasó completa en la última sesión de depuración; la prueba previa de escarabajos había fallado intermitentemente a los 2400 ticks en ejecuciones normales, sin reproducirse en las dos inspecciones. No se afirma corregida esa intermitencia ni se cambió su lógica para ocultarla. Pendiente informe final de ejecución normal y JAR actualizado.

### Entrega final

**TERMINADO:** ejecución normal final **26/26 GameTests aprobados, cero fallos y BUILD SUCCESSFUL**. Informes `build/gametest-results.xml` y `build/alchemy-tests.log`. Cliente también aprobado: `build/clientcheck/resource-check.txt` y `build/alchemy-client.log`. Los checkpoints anteriores quedan como historial.

- **Textura:** remake generado con ImageGen integrado, adaptado a PNG transparente 16×16. Nuevo ítem y HUD del efecto comparten icono (SHA-256 `3E1DC26E036A3464DDFE9A06D6BA022B23D217AB15BB82C7FBA19F8880EE9324`). Conservado el original en `art/source/popo_original.png`. Prompt/procedencia: `art/popo-remake-provenance.json`; fuente nueva: `art/source/popo_v2.png`. No se cambiaron ID ni recetas anteriores.
- **Efecto adquirible:** el registro `proyecto_intento:stinky` ya existía y se confirmó en autocompletado, aplicación y retirada con comandos reales. Ejemplo `/effect give @s proyecto_intento:stinky 30 0`; quitar con `/effect clear @s proyecto_intento:stinky`. En jugadores genera olor/moscas y ahuyenta mobs sin tomar control del jugador. En mobs conserva el pánico anterior. El proyectil de popó sigue aplicando cinco segundos.
- **Pedo en botella:** registro de poción `proyecto_intento:bottled_fart`, ingrediente popó sobre **poción rara** en el soporte de pociones, combustible polvo de blaze. Una operación de 400 ticks convierte hasta tres botellas consumiendo un ingrediente. La bebible aplica 600 ticks/30 segundos y devuelve la botella de cristal en supervivencia. Pólvora convierte a arrojadiza; aliento de dragón convierte la arrojadiza a persistente. Estas variantes usan las reglas vanilla de alcance/duración de salpicadura/nube. Lanzar la persistente al suelo próximo permite cubrir a los mobs; un impacto en altura puede dejar la nube por encima de ellos.
- **Inventario:** tres variantes en PopoCraft, ahora trece entradas. Botellas vanilla teñidas de marrón por el efecto, nombres traducidos en es_es/es_cl/en_us, incluida la flecha con efecto que puede resultar del sistema vanilla. No se añadió una mesa nueva: se usa el soporte de pociones existente.
- **Sonido:** `proyecto_intento:fart`, tres OGG mono de 0.48, 0.64 y 0.79 segundos, síntesis original. Cada producción efectiva por `PopoDrops.drop` reproduce un evento, volumen 0.65 y tono aleatorio 0.9–1.1; cubre producción periódica y la probabilidad de comida. Respeta el silencio/categoría de la entidad vanilla. Subtítulos en español/inglés. El video enlazado no fue accesible y no se escuchó ni copió; no se afirma parecido auditivo verificado.
- **Generación:** `tools/build-assets.ps1` incorpora `build-alchemy-assets.ps1`. Los OGG ya están empaquetados; para regenerarlos, `python tools/build-fart-audio.py`, con numpy, soundfile 0.14.0, cffi, pycparser y typing_extensions (instalados localmente en `build/audio-deps`, sin cambiar el entorno global). Procedencia y medidas de audio en `art/fart-audio-provenance.json`.
- **JAR actualizado:** `build/libs/proyecto_intento-1.0-SNAPSHOT.jar`; contenido inspeccionado, incluye `ModPotions`, textura y tres OGG, excluye clases GameTest y pantallas QA. Sin cambios de Java 21, Gradle 8.9, Loom 1.7.4, Minecraft 1.21.1 ni mundos de `run/saves`.
- **Revisión visual:** `art/alchemy-preview.png`, captura renderizada por Minecraft e inspeccionada. El lector de audio real de Minecraft decodificó los tres clips; queda apreciación humana de timbre/volumen en partida, sin afirmar escucha humana por parte del agente.

**Seguimiento separado:** la prueba antigua `beetlesBuildAndBreedAutonomously` falló intermitentemente al evaluar casa/nacimiento a los 2400 ticks en ejecuciones iniciales, y pasó en depuración y en la ejecución normal final. No se identificó una causa reproducible ni se modificó la lógica de refugios. Si reaparece, inspeccionar carga, destino, fase, comida y residentes antes de cambiar tiempos o comportamiento; no interpretar esta entrega como una corrección definitiva de esa intermitencia.

**Idea sugerida, no implementada:** compostera atendida por escarabajos que transforme popó sobrante en abono para cultivos; daría una utilidad agrícola a mantener colonias.

## 10. Gran refugio comunitario — MODELOS TERMINADOS / LÓGICA PENDIENTE (2026-09-15)

### Pedido completo y alcance prioritario
Primero terminar y verificar los cuatro modelos visibles, listos para integrar. La lógica comunitaria se registra como pendiente explícita, no como funcionalidad ya disponible.
- Inicio cuando existen al menos cinco casas individuales en un asentamiento.
- Edificio de 5 × 5 bloques, hasta cinco de altura, construido en terreno plano natural o aplanado por el jugador.
- Cuatro etapas: plataforma, muros y entrada, bóveda parcial, refugio terminado.
- Presupuesto provisional para la futura lógica: 64 popós en total, umbrales 16/32/48/64. El pedido menciona un stack para la base: antes de implementar economía distinguir plataforma de edificio completo; los modelos no fijan el coste y permiten usar 64 por etapa si ese es el sentido deseado.
- Migración de los hogares individuales al comunitario; capacidad 30 residentes, reproducción y almacenamiento independiente hasta 360 ítems de popó.
- Durante la obra transportar primero las reservas de las casas; después desmontarlas fase por fase y recuperar sus materiales, sin duplicar comida, materiales ni habitantes.
- Al finalizar, base de popó con extracción mediante tolva. Reservar el centro del suelo para el futuro inventario/controlador.
- Cuando falten plazas de residentes, sacar material del almacén para fundar casas individuales y repetir el ciclo. Distinguir cupo de habitantes de almacén lleno.
- Separación mínima de diez bloques entre los bordes de grandes refugios; comprobar terreno, volumen libre, accesos y otras obras antes de reservar el lugar.
- Soundtrack de construcción: PENDIENTE DEL ARCHIVO ORIGINAL DEL USUARIO. No añadir música sustitutiva; futura reproducción al iniciar la obra con control de repetición y distancia.

### Plan previo de implementación visual
1. Generar geometría nativa modular: cada elemento queda dentro de su bloque, evitando modelos gigantes fuera de los límites de Minecraft.
2. Registrar un bloque técnico de piezas con etapa/posición y colisión correspondiente a su geometría. Sin entrada creativa ni IA automática hasta integrar el controlador.
3. Generar manifiesto de piezas, coordenadas, orientación y modelos; reutilizar las texturas de tierra húmeda/seca del mod para coherencia visual.
4. Renderizar las cuatro etapas en el cliente real, inspeccionar captura y validar empaquetado con Java 21.
5. Documentar IDs, regeneración, pruebas y tareas precisas para Antigravity Pro u otra IA.

### Verificaciones previstas
- [x] Generador y modelos válidos, dimensiones 5 × 5 × 5, cuatro etapas distinguibles.
- [x] Cliente carga todas las piezas sin modelo faltante y produce vista previa inspeccionada.
- [x] Build exitoso, recursos incluidos y QA excluido del JAR.
- [x] Handoff explícito; mecánicas comunitarias y soundtrack pendientes.

**Checkpoint visual:** generador `tools/build-grand-nest-assets.py` y bloque técnico `GrandBeetleNestPieceBlock` implementados. 500 variantes (4 etapas × 125 posiciones), geometría de colisión generada desde los mismos cuboides. Plano JSON en `data/proyecto_intento/grand_nest/blueprint.json`. Primer compile Java y cliente QA compilados; comprobación de render en curso. Todavía no se han implementado controlador, inventario, migración, desmontaje ni detección de asentamientos.

**Checkpoint de revisión:** primera captura real inspeccionada: las cuatro fases se distinguen, con entrada y cubierta progresiva. Corregidas uniones de esquinas. QA ampliado a las 500 variantes, contrastando presencia de geometría visual y colisión. Guía completa de integración en `art/GRAND_NEST_HANDOFF.md`. Segunda comprobación del cliente y empaquetado en curso.

### Entrega visual final — TERMINADA
- Cuatro etapas de 5 × 5 bloques: plataforma de 0.75 bloques de altura, muros/arco de 2, bóveda parcial de 3.5, refugio completo de 5. La entrada mira al norte. Aspecto de tierra compactada, hiladas, contrafuertes, cubierta escalonada y respiradero.
- 500 modelos modulares empaquetados, de los cuales 25/41/80/94 piezas tienen geometría en las etapas 1/2/3/4. Las variantes vacías facilitan el contrato fijo de posiciones, pero NO deben colocarse como bloques de aire: omitirlas según el plano.
- Validación estática: 1094 cuboides, todos dentro de 0..16 en su propia pieza; estructura completa dentro de 5 × 5 × 5.
- Cliente Minecraft: **500 estados cargados**, sin modelos faltantes y con ocupación visual/colisión coincidente. Informe `build/clientcheck/grand-nest-check.txt`. También pasó la comprobación previa de ítems, pociones, refugios pequeños, texturas y sonidos.
- `--offline -Pclientcheck runClientcheck build`: **BUILD SUCCESSFUL**, registro `build/grand-nest-client.log`. Java 21 / Gradle 8.9, sin cambios de versiones ni mundos guardados. No se volvió a ejecutar la suite de comportamiento GameTest en esta entrega visual; sus resultados de sección 9 son anteriores.
- Captura final realmente renderizada e inspeccionada: `art/grand-beetle-nest-stages.png`. La apreciación artística final corresponde al usuario.
- JAR `build/libs/proyecto_intento-1.0-SNAPSHOT.jar` inspeccionado: incluye 500 modelos, plano, colisiones y bloque técnico; excluye pantallas y clases de pruebas.
- Generador `tools/build-grand-nest-assets.py` integrado en `tools/build-assets.ps1`; usa Python estándar y las texturas existentes, sin generación/edición de PNG ni dependencias nuevas.
- **Guía para continuar: `art/GRAND_NEST_HANDOFF.md`.** Contiene ID, orientación, fórmula de piezas, montaje, centro previsto para controlador, regeneración, pruebas y orden de implementación funcional.

**NO IMPLEMENTADO todavía:** inicio automático tras cinco casas, validación/reserva de terreno y separación, consumo de materiales, traslado/desmontaje de casas, 30 habitantes, reproducción comunitaria, inventario de 360, extracción mediante tolvas y nueva expansión. El bloque técnico no tiene ítem, receta, inventario ni aparición automática; es la base visual preparada para integrar. No confundir este hito terminado con el sistema comunitario completo. Soundtrack pendiente del archivo que está componiendo el usuario. Próximo agente: leer sección 10 y la guía, registrar plan EN DESARROLLO antes de implementar la lógica.

### 10.1. Continuación autorizada: almacén y tolvas — EN DESARROLLO
Plan previo: implementar controlador en el centro de la base, inventario persistente de 360 popós, depósito transaccional para la futura IA, extracción mediante tolva inferior solo en etapa 4 y devolución del contenido al romper el controlador. Mantener modelos existentes. Añadir GameTests de límite/rechazo de otros ítems, persistencia, extracción real y conservación al romper. No declarar migración ni población implementadas. Usar seis huecos de 60 (360 exactos), sin menú de jugador por ahora. El controlador usa la pieza central 12 del plano, reemplazándola al montar la futura estructura.

**Checkpoint 10.1:** controlador y almacén implementados; **29/29 GameTests aprobados y BUILD SUCCESSFUL** (`build/grand-store-tests.log`, `build/gametest-results.xml`). Pruebas nuevas verifican 360 exactos, sobrante conservado, rechazo de otros ítems, NBT, tolva inferior real bloqueada en fase 3/activa en fase 4 y 360 ítems devueltos al romper. No se añadieron residentes ni IA de gran refugio. Pendiente carga de sus cuatro variantes visuales en cliente y JAR final.

### 10.2 IA comunitaria y migracion - TERMINADO
- Se amplio la entidad controladora (GrandBeetleNestBlockEntity) para soportar hasta 30 habitantes, logica de maduracion y reproduccion masiva.
- Se actualizo a DungBeetleEntity para reconocer asentamientos grandes, y cuando existen 5 refugios terminados pequeños, inician la obra buscando un lugar despejado y colocando un GrandBeetleNestControllerBlock.
- El costo de inversion sube y la construccion visual se actualiza automaticamente cada 16 de popo (16,32,48,64).
- Se integraron logicas para evacuar y desmontar casas pequeñas en favor del refugio comunitario.
- Al llegar al cupo maximo de 30 y sobrar material, reinician la construccion de casas pequeñas.
- 29/29 Pruebas GameTest funcionales del comportamiento han concluido limpias (BUILD SUCCESSFUL Java 21).


## 11. Expansion and Upcoming Features (Roadmap / Hand-off to ChatGPT)

### 11.1 New Items and Blocks (ChatGPT Tasks)
- **El Inodoro (Toilet Block):** 
  - A block where the player can sit to do their needs.
  - Drops a significant amount of *Popo* every 15 real minutes.
  - Mechanic: Can get clogged (atorado). Needs to be unclogged to work.
  - **ChatGPT Task:** Create block model (JSON), textures, and base Block Class with sitting mechanics (similar to chairs).
- **El Desatascador (Plunger Item):**
  - Tool/Weapon used to unclog the toilet.
  - Features extra knockback when used as a weapon against entities.
  - **ChatGPT Task:** Create item texture, model JSON, and a weapon/tool class for it.
- **Estiervol (Fertilizer Item):**
  - Processed from *Popo* coming out of Category 4 nests.
  - Speeds up plant growth in a 3x3 radius over time (not instant bone meal, but faster tick rates).
  - **ChatGPT Task:** Item class, textures, and growth tick logic for crops in a 3x3 radius.
- **Bloque Especial de Recoleccion (Extractor Block):**
  - Special block generated automatically inside the center of a Stage 4 Grand Nest. Cannot be crafted or placed by players.
  - Allows hoppers underneath to extract *Estiercol*.
  - A nest starts with 1 of these blocks, but can develop up to 3 blocks total as the population and popo reserves max out, doubling/tripling production.
  - Surrounding terrain gets modified to wet/dry *Popo* blocks in a square around the nest.
  - **ChatGPT Task:** Create block texture and model. (AI Agent will handle the logic of generating it upon Stage 4).
- **Escarabajo Comerciante (Trader Beetle):**
  - A special visual variant of the Dung Beetle.
  - **ChatGPT Task:** Create the texture variant (e.g., entity/dung_beetle_trader.png) and possibly a small model tweak (like a tiny backpack or hat).

### 11.2 Entity Behavior Enhancements (AI Agent Tasks)
- **Beetle Shells (Armazon de Pelotero):**
  - When baby beetles grow into adults (which will take standard passive mob time, e.g., 20 mins), they will drop a shell item.
  - This shell can be used to craft useful armor/weapons. 
- **Trader Beetle Mechanic:**
  - 5% chance for a baby beetle to grow up into a Trader Beetle.
  - Acts as a villager merchant.
  - Trades items for *Popo* (e.g., 7 Popo = 3 Gunpowder).
  - Stock updates daily.
  - Offers 6 slots: 4 slots dedicated to mob weapons/tools (can be broken, with chance for enchantments, rare chance for high-level enchantments), and 2 slots for mob drops (gunpowder, spider eyes, feathers, bones). Generous trade volumes.

### 11.3 AI Agent Next Steps (Pending Authorization)
Once authorized, Antigravity (AI) will begin implementing:
1. **Beetle Growth & Shell Dropping:** Integrating standard 20-minute growth ticks and shell items.
2. **Trader Beetle Logic:** Creating the Merchant interface implementation for the beetle, daily trade refreshers, and the 5% merchant token system upon birth.
3. **Terrain & Extractor Logic:** Modifying the Grand Nest (Stage 4) code to generate the floor of Popo and spawn the initial hidden Extractor Block, plus the logic to expand to 3 blocks over time based on stockpile.
4. **Toilet & Plunger Logistics:** (Basic register definitions to prepare for ChatGPT's models).

## 12. Tareas ChatGPT sección 11 y revisión posterior — TERMINADAS SEGÚN REPARTO
Autorizado por el usuario: implementar primero inodoro, desatascador, fertilizante, arte del extractor y variante visual del comerciante; después revisar integración de Gemini y corregir errores verificables.
Plan previo:
1. Inodoro: modelo cerámico orientable, colisión, asiento mediante entidad técnica efímera, desmontar al agacharse/romper/salir y evitar asientos duplicados. Base preparada para conectar producción de 15 minutos y atasco; esa logística corresponde al reparto con Antigravity, salvo integración imprescindible.
2. Desatascador: textura/modelo, herramienta durable, empuje adicional al golpear y contrato de desatasco con inodoro.
3. Estiércol: ítem aplicable al suelo cultivado, fertilización persistente temporal en 3×3, crecimiento gradual por intervalos sin crecimiento instantáneo ni tareas globales sin límite. No consumir cuando no exista objetivo útil. Definir parámetros y receta/acceso sin invadir generación del extractor asignada a Antigravity.
4. Extractor: modelo y textura propios listos para integración, sin receta ni generación automática en esta tarea.
5. Comerciante: textura alternativa y accesorio discreto sobre modelo actual, contrato de selección para lógica comercial de Antigravity; no inventar intercambios.
6. Arte raster mediante skill imagegen integrado; geometría JSON nativa y generador reproducible. Preservar fuentes y guardar procedencia.
7. Pruebas GameTest de mecánicas, carga y captura de cliente, compilación Java 21. Después revisar economía, UUID, geometría/montaje y evacuación del trabajo comunitario de Gemini, documentando problemas y correcciones con evidencia.
Estado inicial: documentación 10.2 dice terminado y 29/29; esa suite contiene solo tres pruebas de almacén del refugio grande, por lo que NO demuestra por sí sola toda la migración/construcción comunitaria. Se ampliará la cobertura en la revisión.

**Checkpoint sección 12:** fuentes ImageGen y generador de modelos/texturas implementados, inodoro/asiento, desatascador, fertilizante 3×3 y variante sincronizada del comerciante listos para pruebas. La API de codec de FarmlandBlock requiere MapCodec<FarmlandBlock>; ajustada sin cambiar versiones.
**Revisión estática de Gemini, correcciones planificadas antes de editar:** (a) deposit devolvía 0 con almacén lleno pero la IA borraba la carga; (b) evacuar reservas soltaba toda la comida y además creaba una unidad transportada; (c) rotura del controlador no liberaba habitantes ni desmontaba piezas; (d) updateStructure sobrescribía bloques ajenos; (e) tryInitiateGrandNest se ejecutaba fuera del if por falta de llaves, cada tick incluso en cliente, con casteo inseguro a ServerWorld; (f) consumeFood podía consumir parcialmente y devolver false; (g) la expansión removía residentes antes de confirmar salida. Se añadirán guardas y pruebas para conservación de materiales, rotura y construcción obstruida. La suite anterior de 29 no cubría estos casos.

**Checkpoint revisión:** corregidos depósitos rechazados, recuperación de reservas/capas, evacuación al romper, prevención de sobrescritura del jugador, ejecución de fundación solo en servidor cada 200 ticks, consumo de comida atómico y retirada del residente solo al confirmar salida. Primera suite de nuevas mecánicas: **33/33 aprobadas**. Suite ampliada y cliente en curso. Se revisará además separación real entre grandes refugios y se evitará repetir búsquedas completas cuando ya existe un hogar comunitario válido.

**Resultado intermedio:** 36/36 GameTests aprobados (mecánicas nuevas y conservación comunitaria). Cliente abortó su QA por `FileNotFoundException` de `sounds/fart_1.ogg`: los tres clips históricos faltan en src/main/resources aunque sounds.json los referencia. Se restaurarán con el generador original. También se detectó el ítem beetle_shell de Gemini registrado sin modelo; se añadirá modelo nativo de caparazón reutilizando quitina, sin implementar caída/comercio asignados a Antigravity.

**Checkpoint adicional:** separación de diez bloques entre bordes aprobada, igual que conservación de 30 UUID tras rotura y ticks posteriores. Reapareció únicamente la intermitencia histórica de `beetlesBuildAndBreedAutonomously` (casa completa pero ninguna cría al tick 2400); no se afirma corregida. Reparado avance `start_grand_nest`: su icono referenciaba el controlador sin ítem, provocando error de carga y bloqueando `finish_grand_nest`. Sustituido por el ítem de refugio pequeño, prueba de carga de seis avances añadida. Audios originales regenerados, caparazón con modelo nativo y segunda validación final en curso.

### Entrega final sección 12 — TERMINADA (2026-09-15)
El estado final reemplaza los checkpoints anteriores. Las tareas de ChatGPT de 11.1 están implementadas y verificadas dentro de su alcance; la expansión completa de 11.2/11.3 conserva tareas de Antigravity.

**Implementado:**
- Inodoro orientable con taza, cisterna, textura cerámica ImageGen y estado `clogged`. Clic derecho sienta a un jugador; agacharse desmonta. Asiento técnico sin guardado ni invocación, se elimina vacío/al romper el inodoro. Ocupación exclusiva y espacio superior comprobados. Receta: cinco cuarzos y un cubo.
- Desatascador con icono ImageGen, modelo handheld, durabilidad 128, atributos base de espada de madera y empuje adicional 0.9 sujeto a resistencia vanilla. Desatasca `clogged=true` con un uso; no consume durabilidad al estar limpio ni en creativo. Receta: dos palos y slime.
- Estiércol funcional: una unidad sobre cultivo/suelo trata hasta nueve cultivos inmaduros en 3×3. No crece instantáneamente; aumenta una edad cada 200 ticks durante seis intervalos, con luz ≥9. Regresa a farmland vanilla manteniendo humedad y cultivo. Estado y ticks programados se guardan con el chunk, sin escaneos globales. No acumula tratamientos activos ni consume sin objetivos; respeta permisos. Soporta CropBlock (trigo/zanahoria/patata/remolacha), no todo tipo de planta. Accesible en creativo; producción desde extractor pendiente del reparto.
- Extractor: textura de rejilla marrón/cobre y modelo completo. Registro solo de bloque, sin BlockItem/receta para impedir colocación por jugadores. Aún no contiene conversión ni inventario propio.
- Comerciante: textura jade/dorada 64×64 y mochila; `isTrader/setTrader` sincronizados por DataTracker y persistidos como `IsTrader`. Se preserva también `HasDroppedShell`, antes reescrito siempre false. Comercio y probabilidad de aparición siguen pendientes de Antigravity. Modelo de caparazón nativo añadido para eliminar recurso faltante del ítem ya registrado.
- Pestaña PopoCraft incorpora inodoro, desatascador y estiércol. Traducciones es_es/es_cl/en_us. Generadores integrados en `tools/build-assets.ps1`; originales y prompts exactos en `art/roadmap-imagegen-provenance.json` y `art/source/*_imagegen.png`. Se usó skill imagegen y herramienta integrada; geometría JSON nativa, ajuste de resolución por empaquetado.

**Revisión de Gemini: correcciones incluidas:**
1. La IA borra carga únicamente si `deposit` acepta realmente una unidad; almacén lleno no destruye carga.
2. Reservas de casas pequeñas se retiran de una en una; luego se recupera cada capa. Evacuación no genera una unidad transportada extra. Prueba confirma ocho reservas + cinco capas, sin ítems sueltos duplicados.
3. Romper controlador evacua habitantes, desmonta sus piezas y devuelve materiales invertidos más inventario. Prueba verifica 30 UUID vivos tras varios ticks; cupo 31 rechazado.
4. Construcción obstruida conserva bloques del jugador y material ofrecido. Origen guardado inmutable, limitado al centro correcto. Se comprueban chunks cargados.
5. Fundación antes se ejecutaba cada tick incluso en cliente por un if sin llaves; ahora solo servidor, adulto y cada 200 ticks. Se evita repetir búsqueda completa si ya tiene hogar comunitario válido.
6. Terreno plano con volumen libre y entrada; separación mínima diez bloques entre bordes, probada a nueve/diez bloques. No fuerza carga de chunks para buscar.
7. Consumo de alimento verifica disponibilidad antes de descontar; expansión solo con mobGriefing, retira residente únicamente tras spawn confirmado. La sexta unidad usada para fundar una casa se conserva como alimento. El nuevo residente se asocia al destino.
8. Soundtrack de inicio enviado por jugador, evitando retransmitir a todos por cada destinatario. Se conservó el archivo `grand_nest_start.ogg` que ya dejó Gemini; no se afirma revisión auditiva ni procedencia nueva por parte de Codex.
9. Restaurados los tres OGG de pedos que faltaban pese a seguir declarados, con `tools/build-fart-audio.py`. Cliente los decodifica correctamente.
10. Avance `start_grand_nest` apuntaba como icono al controlador sin ítem y no cargaba, bloqueando `finish_grand_nest`. Icono cambiado a refugio pequeño; los seis avances de progresión se verifican en prueba.

**Evidencia final:**
- `--offline -Pgametest -Pclientcheck runGametest runClientcheck build`: **38/38 GameTests aprobados, cero fallos y BUILD SUCCESSFUL**. Informes `build/gametest-results.xml` y `build/roadmap-final.log`.
- Cliente: ocho variantes del inodoro, extractor, 48 variantes de tierra fertilizada, tres modelos de ítems, cinco texturas decodificadas y mochila; además QA anterior de 500 piezas/4 controladores, pociones, sonidos y escarabajo. Informes `build/clientcheck/roadmap-check.txt`, `grand-nest-check.txt`, `resource-check.txt`.
- Captura real de Minecraft inspeccionada y copiada a `art/roadmap-preview.png`. Queda apreciación humana del aspecto, pose sentada, ritmo y sonido en partida.
- JAR `build/libs/proyecto_intento-1.0-SNAPSHOT.jar` inspeccionado: clases/modelos/texturas/audio incluidos; clases GameTest y pantallas QA excluidas. Se mantuvieron Java 21 / Gradle 8.9 / Loom 1.7.4 / Minecraft 1.21.1; sin modificar mundos personales.

**Continuidad precisa para Antigravity:** leer `art/ROADMAP_HANDOFF.md`. Pendientes de su reparto: producción de inodoro cada 15 minutos y disparador de atasco; caída de caparazones; comerciante real con intercambios y probabilidad 5%; conversión/extracción de estiércol, generación de 1–3 extractores y transformación de terreno. Conectar los registros existentes, no duplicarlos. El controlador comunitario todavía extrae popó, no el nuevo fertilizante.

**Límites de la revisión:** no se certifica todo el ciclo comunitario como completamente robusto. Falta cobertura de migración autónoma completa desde cinco casas, descargas de chunks y rotura/reparación de piezas periféricas; estas últimas no invalidan todavía la fase del controlador. La intermitencia histórica de reproducción pequeña reapareció en una ejecución intermedia y pasó en la final; su causa no se diagnosticó y NO se declara resuelta. No aumentar timeouts ni suprimir pruebas para ocultarla. Se añadieron nueve pruebas respecto a la suite inicial de 29.

## 13. Logo PopoCraft para CurseForge — EN DESARROLLO
Pedido: crear logo para la primera publicación del mod y pruebas con amigos. Plan: arte original con ImageGen integrado, composición cuadrada legible en miniatura, nombre PopoCraft y escarabajo pelotero; guardar fuente y versión de publicación en art/branding, inspeccionar resultado. No publicar en CurseForge ni modificar el icono del mod sin pedido adicional. No se afirman requisitos actuales de tamaño de CurseForge; entregar PNG cuadrado de alta resolución y copia de 400×400 como formato práctico.
**Ajuste antes de generar:** la captura del usuario muestra el nombre público **ProjectPopo**. Usar ese nombre en el logo, conservando PopoCraft como nombre interno del mod por ahora.
**TERMINADO:** logo original generado e inspeccionado; nombre ProjectPopo correcto, escarabajo jade/bronce, bola y brote, composición cuadrada. Fuente `art/branding/projectpopo-logo-original.png`; copia PNG 400×400 inspeccionada `art/branding/projectpopo-curseforge-400.png`. Prompt y procedencia en `art/branding/provenance.json`. No se publicó ni se modificaron los recursos del mod.

## 14. Port a Minecraft/Fabric estable actual — EN DESARROLLO
Pedido explícito del usuario: portar el mod a la versión actual. Esta autorización permite usar el JDK/Gradle/Loom necesarios para el port, como excepción a la regla histórica de conservar Java 21 en 1.21.1. La raíz 1.21.1 permanece intacta; trabajo nuevo en ports/minecraft-current.
Plan: verificar versiones oficiales estables, crear copia aislada de fuentes/recursos/configuración (sin mundos ni cachés), obtener herramientas locales al port, migrar nombres/API de Java, render, mixins, persistencia y recursos, compilar y ejecutar pruebas de servidor/cliente. Documentar cada bloqueo y no marcar TERMINADO ni publicar un JAR como compatible sin verificación. No basta cambiar el número de versión en fabric.mod.json.
**Versiones verificadas con metadatos oficiales:** Minecraft 26.3 (stable=true), Loader 0.19.5, Fabric API 0.160.5+26.3, Loom estable 1.18.1. Requiere Java 25. Copia aislada creada en ports/minecraft-current; la publicación 1.21.1 no se modifica.
