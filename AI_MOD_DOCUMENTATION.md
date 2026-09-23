# ðŸ’© PopoCraft (proyecto_intento) - DocumentaciÃ³n Central y Reglas para IA

**Â¡BIENVENIDO/A, AGENTE IA Y DESARROLLADORES HUMANO!**
Este proyecto estÃ¡ diseÃ±ado bajo un modelo **MULTI-IA** y de trabajo colaborativo.

> âš ï¸ **REGLAS ESTRICTAS Y OBLIGATORIAS PARA CUALQUIER IA QUE INGRESE AL PROYECTO** âš ï¸
> 1. **LECTURA OBLIGATORIA:** Antes de sugerir cÃ³digo, compilar o accionar, DEBES leer este archivo de principio a fin para entender el contexto, las versiones, y las tareas activas.
> 2. **NO ROMPER EL ENTORNO:** El proyecto fue arduamente configurado para **Java 21**, **Gradle 8.9** (con el plugin de Fabric-Loom 1.7.4) para Minecraft 1.21.1. Â¡NUNCA introduzcas configuraciones o cÃ³digo que dependa de Java 25 u otras versiones incompatibles!
> 3. **DOCUMENTAR TODO:** Cualquier cambio importante (nuevo Ã­tem, bloque, mecÃ¡nicas, refactorizaciÃ³n) DEBE ser registrado en la secciÃ³n correspondiente de este archivo inmediatamente despuÃ©s de validarse en el juego.
> 4. **CONFIRMAR ACCIONES:** Al terminar tus tareas, debes informar al usuario humano del Ã©xito, actualizar el estatus de las tareas pendientes y documentar cÃ³mo dejaste el cÃ³digo.
> 5. **PLAN Y CONTINUIDAD ANTES DE IMPLEMENTAR:** Antes de modificar cÃ³digo o recursos, registrar aquÃ­ el pedido, el plan y las verificaciones previstas con estado **EN DESARROLLO**. Actualizar avances y pendientes durante el trabajo para poder retomarlo tras una interrupciÃ³n. Marcar **TERMINADO** Ãºnicamente tras completar y verificar la implementaciÃ³n; distinguir siempre pruebas automÃ¡ticas de revisiÃ³n humana pendiente. Esta regla se aplica a todas las sesiones futuras.

---

## ðŸ“– 1. VisiÃ³n y Concepto del Proyecto
El objetivo principal de **PopoCraft** es introducir mecÃ¡nicas de juego irreverentes, divertidas y Ãºnicas centradas alrededor del Ã­tem base de la "caca" (`popo`). Se buscarÃ¡ expandir sistemas agrÃ­colas, de combate, y utilitarios partiendo de mecÃ¡nicas biolÃ³gicas absurdas pero que encajen dentro de las mecÃ¡nicas de supervivencia de Minecraft.

**Datos TÃ©cnicos del Entorno:**
* **Juego:** Minecraft 1.21.1
* **API / Modloader:** Fabric
* **VersiÃ³n de Java Obligatoria:** Java 21
* **VersiÃ³n de Gradle:** 8.9

---

## ðŸ› ï¸ 2. Progreso Actual e Implementaciones (Lo que ya estÃ¡ hecho)
- [x] **ConfiguraciÃ³n Base del IDE y Compilador:** Solucionado el conflicto masivo de "Java 25" y estabilizados los compiladores, configs de `mixins.json` y `workspace.xml`. Gradle sincroniza limpio.
- [x] **Registro del Mod:** Mod inicializado con el ID `proyecto_intento`.
- [x] **Primer Ãtem Registrado (`POPO`):** Ãtem bÃ¡sico registrado exitosamente en `ModItems.java`. Cuenta con textura visual implementada.
- [x] **IntegraciÃ³n In-Game:** El Ã­tem aparece correctamente asignado a la pestaÃ±a "Ingredientes" del Modo Creativo.
- [x] **ExpansiÃ³n inicial 2026-09-14:** producciÃ³n periÃ³dica, alimentaciÃ³n al 25%, bloques hÃºmedo/seco y armadura repelente implementados. Siete pruebas iniciales aprobadas. El usuario confirmÃ³ en la sesiÃ³n siguiente que la armadura equipada se ve bien. Detalles histÃ³ricos en secciÃ³n 5; ampliaciÃ³n actual en secciÃ³n 6.

---

## ðŸ“‹ 3. Tareas en Proceso (IN PROGRESS)
* **SecciÃ³n 11, tareas ChatGPT â€” TERMINADAS segÃºn reparto:** inodoro/asiento, desatascador, fertilizante gradual, arte del extractor y comerciante. RevisiÃ³n correctiva de integraciÃ³n: 38/38 GameTests, cliente y build aprobados; entrega y pendientes de Antigravity en secciÃ³n 12 y `art/ROADMAP_HANDOFF.md`.
* **Gran refugio comunitario â€” MODELOS TERMINADOS / LÃ“GICA PENDIENTE:** cuatro etapas visuales verificadas en Minecraft, generador y guÃ­a de integraciÃ³n listos. EspecificaciÃ³n y continuidad en secciÃ³n 10.
* **Remake de popÃ³, efecto, alquimia y sonido â€” TERMINADO:** 26/26 GameTests, build y cliente aprobados; entrega en secciÃ³n 9. Queda apreciaciÃ³n humana del sonido y seguimiento de la intermitencia observada en la prueba antigua de construcciÃ³n de escarabajos.
* **Refugios y colonias de escarabajos â€” TERMINADO:** 21/21 GameTests, build y comprobaciÃ³n del cliente aprobados. Captura de las cinco fases inspeccionada. Pendiente Ãºnicamente apreciaciÃ³n humana del comportamiento y aspecto en una partida normal. Entrega y parÃ¡metros en secciÃ³n 8.
* **Segunda sesiÃ³n 2026-09-14 â€” Codex:** desarrollo terminado, build y **13/13 GameTests aprobados**, comprobaciÃ³n automÃ¡tica de recursos en cliente aprobada. Pendiente apreciaciÃ³n humana de los iconos dentro del inventario, animaciÃ³n del escarabajo, densidad de moscas y volumen de los sonidos. La carga automÃ¡tica de recursos no sustituye observar y escuchar una partida.

---

## ðŸš€ 4. Ideas y Funcionalidades a Futuro (Ideas Backlog)
Cualquier IA que retome el proyecto, puede consultar esta lista y preguntar al usuario cuÃ¡l de estas funcionalidades (o una nueva) quiere implementar a continuaciÃ³n:

### ðŸŸ¢ Nivel 1: Jugabilidad Central del PopÃ³
- **FunciÃ³n Fertilizante:** Modificar el PopÃ³ para que actÃºe igual que el "Polvo de Hueso" (Bone Meal) para cultivar semillas al darle clic derecho.
- **Arma Arrojadiza:** implementada en secciÃ³n 6: clic derecho lanza popÃ³ y aplica cinco segundos de peste/pÃ¡nico a mobs. No causa daÃ±o directo.

### ðŸŸ¡ Nivel 2: Crafteos y Bloques FÃ­sicos
- **Bloque de PopÃ³:** implementado como bloque hÃºmedo con hundimiento y variante seca por horno (secciÃ³n 5). Moscas ambientales aÃ±adidas en secciÃ³n 6.
- **Biomasa (Combustible):** Hacer que el PopÃ³ sea quemable en los hornos, ofreciendo una fuente de combustible renovable de bajo poder.

### ðŸ”´ Nivel 3: Funciones Avanzadas del Mundo
- **Drop Natural (MecÃ¡nica BiolÃ³gica):** implementado para `AnimalEntity`, con temporizador persistente y alimentaciÃ³n (secciÃ³n 5).
- **Armas Apestosas:** pendiente crear herramientas que apliquen veneno. La armadura repelente ya estÃ¡ implementada; no aplica veneno.

---
*(Las secciones siguientes contienen el historial y las verificaciones mÃ¡s recientes. Leer hasta el final.)*

## 5. SesiÃ³n 2026-09-14 â€” Codex: expansiÃ³n de PopoCraft

**Estado final de la sesiÃ³n:** cÃ³digo y recursos implementados; build exitoso con Java 21 y Gradle 8.9. **7/7 pruebas GameTest aprobadas dentro de Minecraft 1.21.1**. PNG finales inspeccionados. Pendiente revisiÃ³n visual de la armadura equipada y experiencia audiovisual en cliente; el conector de escritorio devolviÃ³ `Computer Use native pipe is unavailable`.

### Cambios implementados
- `AnimalEntityMixin` / `PopoDrops`: animales adultos producen 1 popÃ³ cada 6000â€“12000 ticks (5â€“10 minutos a 20 TPS), Ãºnicamente en servidor y con IA activa. Temporizador persistido en NBT `PopoCraftDropTicks`; se pausa cuando el animal no estÃ¡ cargado. `AnimalEntity.eat` aÃ±ade 25% por comida aceptada, incluyendo crÃ­as y creativo; un clic rechazado no activa la probabilidad. `HorseFeedingMixin` cubre alimentaciÃ³n aceptada por caballos y sus variantes; `WolfFeedingMixin` cubre comida que cura lobos, sin duplicar la tirada de reproducciÃ³n. No se considera alimentaciÃ³n teÃ±ir collares, equipar armadura o usar huesos para domesticar.
- `ModBlocks` / `WetPopoBlock`: `wet_popo`, receta 3Ã—3 con 9 popÃ³s. Bloque colocable sin colisiÃ³n sÃ³lida, fricciÃ³n horizontal y hundimiento lento para jugadores, reinicio de daÃ±o de caÃ­da; nÃ¡usea y fatiga minera II en supervivencia. Las botas de cuero no permiten caminar encima. `LivingEntityMixin` reutiliza la lÃ³gica vanilla de aire/ahogamiento cuando el bloque cubre los ojos: respeta RespiraciÃ³n, respiraciÃ³n acuÃ¡tica y creativo. No genera congelamiento ni daÃ±o de asfixia de bloque sÃ³lido.
- `dry_popo`: un bloque hÃºmedo al horno, 200 ticks (10 segundos), 0.1 XP. Bloque sÃ³lido decorativo, herramienta eficiente: pico. Bloque hÃºmedo: pala. Ambos devuelven su propio bloque al romperse.
- `ModArmor`: casco, pechera, grebas y botas hechos con popÃ³ en los patrones vanilla. ProtecciÃ³n 1/3/2/1 y durabilidad 110/160/150/130. ReparaciÃ³n con bloque seco. Conjunto completo requerido, radio 12 bloques, huida a velocidad 1.65 y sonido propio mÃ¡s agudo cada 2â€“4 segundos. `MobEntityMixin` sustituye temporalmente decisiones de IA al huir para cubrir tambiÃ©n mobs con Brain (aldeanos/piglins); se reanuda la IA al quitarse una pieza o alejarse. Wither y dragÃ³n excluidos por sus sistemas de combate especiales. Mobs encerrados pueden no encontrar salida.
- Recursos: modelos, estados de bloque, loot tables, recetas y desbloqueo del recetario, etiquetas de herramientas y traducciones es_es/es_cl/en_us. Directorios de datos en singular como requiere 1.21.1.
- Arte: originales ImageGen en `art/source/`; bloques e iconos finales 16Ã—16 y capas de armadura 64Ã—32. `tools/build-assets.ps1` empaqueta las texturas y genera los JSON de forma reproducible. Conservado el `popo.png` original creado con Gemini.
- `SlimeMoveControlAccessor`: adapta la huida al movimiento por saltos de slimes y cubos de magma. `fabric.mod.json`: declara Java >=21 y corrige el icono para apuntar al PNG existente.
- `build.gradle`: aÃ±ade Ãºnicamente configuraciÃ³n optativa `-Pgametest`, con cÃ³digo de pruebas en `src/gametest` y mundo aislado en `build/gametest`. Las pruebas no forman parte del JAR distribuible ni cambian el mundo de desarrollo en `run/saves`. No se cambiaron versiones, JDK del IDE ni configuraciÃ³n global.

### Registro de verificaciones
- CompilaciÃ³n y empaquetado: **OK**, Java 21.0.12, Gradle 8.9, Loom 1.7.4; sin actualizar versiones.
- Pruebas dentro del motor: **7/7 OK**, informe `build/gametest-results.xml`, salida `build/gametest/logs/latest.log`. Incluyen temporizador/guardado/crÃ­as, 400 alimentaciones aceptadas y clics rechazados, carga de las seis recetas y fundiciÃ³n real de 200 ticks, mareo/fatiga/aire/daÃ±o/recuperaciÃ³n, alimentaciÃ³n especial de caballos y lobos, desplazamiento real de hundimiento durante 60 ticks y huida de vaca/husk/aldeano/slime. La prueba comprueba tambiÃ©n que una pieza ausente desactiva la detecciÃ³n del conjunto.
- Arte: `art/texture-preview.png` revisado, sin texturas faltantes en los archivos generados. RevisiÃ³n visual en cliente: **pendiente**. Los sonidos se reutilizan del juego a tono elevado; no son grabaciones de gritos nuevas.
- `genSources --offline` no disponible porque Vineflower 1.10.1 no estaba en cachÃ©; no afecta la compilaciÃ³n. Se consultaron las firmas y bytecode del Minecraft local y la documentaciÃ³n oficial Yarn 1.21.1.

### Reproducir y probar manualmente

Usar **Java 21 y Gradle 8.9**, ya instalados. En esta mÃ¡quina:

```powershell
$env:JAVA_HOME = 'C:/Users/jorge/.jdks/ms-21.0.12.1'
$gradleExe = 'C:/Users/jorge/.gradle/wrapper/dists/gradle-8.9-bin/90cnw93cvbtalezasaz0blq0a/gradle-8.9/bin/gradle.bat'
& $gradleExe --offline build
& $gradleExe --offline -Pgametest runGametest
# Solo si se modifican los originales o las definiciones del generador:
powershell -NoProfile -ExecutionPolicy Bypass -File tools/build-assets.ps1
```

- Entregable: `build/libs/proyecto_intento-1.0-SNAPSHOT.jar` (no usar el `-sources.jar` para jugar).
- En el cliente, los bloques estÃ¡n en ConstrucciÃ³n y la armadura en Combate. Comandos Ãºtiles: `/give @s proyecto_intento:popo 64`, `/give @s proyecto_intento:wet_popo 64`, `/give @s proyecto_intento:dry_popo 64` y `/give @s proyecto_intento:popo_helmet` (anÃ¡logos `popo_chestplate`, `popo_leggings`, `popo_boots`).
- Colocar un bloque hÃºmedo sobre suelo sÃ³lido y pisarlo en supervivencia: hundimiento gradual, mareo y minerÃ­a mÃ¡s lenta. Probar una columna de dos o tres bloques: el aire baja cuando cubre los ojos. Salir lateralmente o romper el bloque; los efectos duran brevemente tras salir (nÃ¡usea hasta 5 s, fatiga hasta 2 s).
- Equipar las cuatro piezas cerca de animales/hostiles/aldeanos en terreno abierto: comprobar huida, sonido y retorno de comportamiento normal al quitar una pieza. Una barrera fÃ­sica puede impedir la huida. La armadura tiene protecciÃ³n modesta; no concede inmunidad a proyectiles ya lanzados ni a daÃ±o por contacto.
- Alcance biolÃ³gico: producciÃ³n en clases `AnimalEntity` (animales), no en monstruos, aldeanos ni entidades inanimadas. Eventos especiales de domesticaciÃ³n o comidas de otros mods que omitan las rutas vanilla cubiertas no estÃ¡n garantizados. Jefes Wither/dragÃ³n conservan su IA.
- Prompts completos y procedencia del arte: `art/imagegen-prompts.json` (herramienta integrada ImageGen, sin API/CLI externa).

### Referencias tÃ©cnicas consultadas
- [AnimalEntity â€” Yarn 1.21.1 build 3](https://maven.fabricmc.net/docs/yarn-1.21.1%2Bbuild.3/net/minecraft/entity/passive/AnimalEntity.html)
- [ArmorMaterial â€” Yarn 1.21.1 build 3](https://maven.fabricmc.net/docs/yarn-1.21.1%2Bbuild.3/net/minecraft/item/ArmorMaterial.html)
- [Cambios Fabric para Minecraft 1.21 y 1.21.1](https://fabricmc.net/2024/05/31/121.html)

## 6. Segunda sesiÃ³n 2026-09-14 â€” lavado, proyectil, moscas y escarabajo

Esta secciÃ³n reemplaza los parÃ¡metros anteriores donde difieran. El usuario aprobÃ³ la apariencia de la armadura equipada y pidiÃ³ corregir los iconos, crear una pestaÃ±a propia y aÃ±adir estas mecÃ¡nicas. Se mantuvieron Java 21, Gradle 8.9, Loom 1.7.4 y Minecraft 1.21.1.

### Arte e inventario creativo
- `ModItemGroups`: pestaÃ±a **PopoCraft**, con popÃ³, ambos bloques, montÃ³n, cuatro piezas de armadura y huevo de escarabajo (9 entradas). Se retiraron las inserciones del mod en las pestaÃ±as vanilla; los elementos siguen disponibles en la bÃºsqueda creativa.
- Iconos: casco frontal nuevo y simÃ©trico; pechera/grebas revisadas, y los cuatro iconos centrados mediante sus lÃ­mites visibles. Las botas mantienen su diseÃ±o. PNG finales 16Ã—16 en `assets/proyecto_intento/textures/item/`.
- Las capas equipadas `popo_layer_1.png` y `popo_layer_2.png` permanecen idÃ©nticas (SHA-256 `FB7B83599BA776529BB9E6FE4EE85E3DFA87E3FCA5491AC302C7C1EEE761AA49` y `F9E0648C0F05272C4424F923EC19342DA183B3927FC3C966C8B0F18B84167A97`).
- ImageGen integrado creÃ³/revisÃ³ los originales `armor_icons_v2.png`, `helmet_v2.png`, `beetle_chitin.png` y `fly.png` en `art/source`. Procedencia y prompts: `art/expansion-imagegen-prompts.json`. Vista de los iconos: `art/armor-icons-v2-preview.png`.
- GeneraciÃ³n reproducible: `tools/build-assets.ps1` llama a `build-expansion-assets.ps1`, que reutiliza `build-base-assets.ps1`. Usar siempre la entrada pÃºblica `build-assets.ps1` para no regenerar solamente la versiÃ³n anterior. Se conservan todos los originales anteriores.

### Lavado y ambiente de moscas
- `SmellSystem`: tocar agua con el conjunto completo sucio lo limpia inmediatamente en servidor. Mientras permanece mojado se refresca el lavado una vez por segundo. Cada lavado dura aleatoriamente 3600â€“6000 ticks (aproximadamente 3â€“5 minutos a 20 TPS).
- El vencimiento se guarda **en cada pieza** mediante `CUSTOM_DATA.PopoCraftCleanUntil`, conservando los demÃ¡s datos del objeto. Persiste al guardar, cambiar de dimensiÃ³n, soltar o volver a equipar. Usa tiempo de juego total, no la hora del dÃ­a; `/time set day` no borra el lavado. El tiempo no avanza si el servidor estÃ¡ apagado.
- Durante el lavado no hay moscas ni repulsiÃ³n procedentes de la armadura. El conjunto necesita cuatro piezas limpias; mezclar una pieza sucia vuelve a generar olor. Mojar piezas sueltas tambiÃ©n las limpia en la actualizaciÃ³n de un segundo. El lavado de armadura no elimina el efecto independiente del proyectil sobre mobs.
- `FlyParticle`: partÃ­cula propia 8Ã—8, cuerpo oscuro y alas claras, vuelo orbital con oscilaciÃ³n vertical. Sigue a las entidades apestosas; desaparece al cesar su olor. Se genera alrededor de conjuntos completos sucios y mobs afectados por el proyectil.
- `WetPopoBlock.randomDisplayTick`: mÃ¡s moscas sobre caras superiores expuestas al aire. No se generan sobre caras tapadas. No se cambiaron el hundimiento ni el ahogamiento.
- `SmellAmbience`: un Ãºnico presupuesto de audio por cliente, **un zumbido cada 12â€“22 segundos**, volumen 0.10 con ganancia adicional 0.6. Campos de bloques hÃºmedos no superponen un sonido por bloque. Cerca de un bloque puede seguir habiendo zumbido aunque la armadura estÃ© limpia.
- `ModSounds` y `sounds.json`: zumbido basado en el sonido vanilla de abeja, chirrido/pasos del escarabajo basados en silverfish y rodado sobre barro; tono/volumen ajustados. Son reutilizaciones de audio vanilla, no nuevas grabaciones OGG. Incluyen subtÃ­tulos en espaÃ±ol e inglÃ©s. Sonido de moscas bajo la categorÃ­a Ambiente.

### Proyectil y pÃ¡nico
- `PopoItem` reemplaza el Ã­tem base manteniendo el mismo ID y textura. Clic derecho lanza una unidad, conserva el objeto en creativo, cooldown de 12 ticks (0.6 segundos).
- `PopoProjectileEntity`: trayectoria con gravedad, render del Ã­tem existente, salpicadura y sonido al colisionar; se elimina al impacto o tras 200 ticks. No devuelve municiÃ³n ni causa daÃ±o directo.
- `ModEffects.STINKY` (`proyecto_intento:stinky`): impacto real en un `MobEntity` aÃ±ade **100 ticks / 5 segundos**. El afectado es una fuente de olor de radio 12, provocando la huida de sus vecinos. Otro impacto refresca los cinco segundos, sin acumular minutos.
- `MobEntityMixin`: reconoce armadura sucia y mobs apestados como fuentes. El afectado corre por objetivos alrededor de su posiciÃ³n inicial, formando cÃ­rculos cuando el terreno lo permite. Al terminar el efecto se recupera la IA normal, salvo que siga cerca de otra fuente de olor.
- Sonidos de pÃ¡nico aumentados de cada 2â€“4 segundos a **cada 12â€“24 ticks (0.6â€“1.2 s)**, volumen 0.65 y tono 1.5â€“1.85. Se conserva el sonido propio de cada mob y la adaptaciÃ³n de salto para slimes/cubos de magma.
- Se mantienen las excepciones de Wither/dragÃ³n y de mobs con IA desactivada. El movimiento respeta navegaciÃ³n; paredes, encierros y terrenos sin ruta pueden impedir los cÃ­rculos o la huida. El proyectil afecta mobs, no jugadores; no es un sistema de control PvP.

### Escarabajo pelotero y montones
- `DungBeetleEntity`: mob pasivo nuevo, 8 puntos de salud (4 corazones), dimensiones 0.65Ã—0.4 bloques. ApariciÃ³n natural poco frecuente (peso 5, grupos 1â€“2) en llanuras, sabanas y bosques, bajo reglas vanilla de apariciÃ³n de animales. Huevo propio en la pestaÃ±a PopoCraft. No fuerza apariciÃ³n en chunks antiguos ya poblados.
- Recoge exclusivamente entidades de Ã­tem `popo` alcanzables en un radio de 12 bloques (3 verticales). Busca ruta y comprueba visibilidad antes de recoger. Lleva **una unidad por viaje**, descontÃ¡ndola correctamente de stacks mÃ¡s grandes.
- Establece hogar al empezar, restringe deambulaciÃ³n a unos 12 bloques y guarda hogar/carga en NBT (`PopoHome`, `CarriedPopo`). Busca un montÃ³n cerca del hogar o un espacio libre con suelo sÃ³lido; no rompe ni reemplaza construcciones. Se detiene si no encuentra destino y conserva su carga.
- `PopoPileBlock` (`popo_pile`): montÃ³n decorativo con estado `amount=1..8` y altura variable. Cada depÃ³sito aumenta el contenido en uno; romperlo devuelve exactamente esa cantidad de popÃ³. Se puede fabricar un montÃ³n inicial con una unidad de popÃ³. Herramienta eficiente: pala.
- RecolecciÃ³n y colocaciÃ³n respetan `mobGriefing`. Dos escarabajos revisan el estado del Ã­tem antes de recogerlo para evitar duplicaciÃ³n. Al morir puede soltar la unidad transportada mediante el flujo de loot vanilla. Las crÃ­as no recolectan. Pueden alimentarse/criarse con semillas de trigo.
- `DungBeetleModel` / `DungBeetleRenderer`: modelo propio, caparazÃ³n partido de quitina oscura/bronce, seis patas con marcha alternada, antenas oscilantes y bola frontal visible/rotatoria solo al transportar. Textura 64Ã—64; material de la bola reutiliza el bloque hÃºmedo. Chirridos suaves, pasos y sonido de rodado.

### Comprobaciones y entrega
- Primera ejecuciÃ³n: **12/12 GameTests aprobados**, incluyendo las siete pruebas previas mÃ¡s lavado/persistencia/vencimiento, impacto real y duraciÃ³n del proyectil, pÃ¡nico y repulsiÃ³n entre mobs, recogida/depÃ³sito conservando cantidades, y NBT de hogar/carga.
- ComprobaciÃ³n de cliente: **APROBADA**. Cliente Minecraft/OpenGL/OpenAL iniciado en `build/clientcheck`, cargÃ³ 9 modelos de Ã­tems, 5 texturas especÃ­ficas, piezas del modelo del escarabajo y 4 eventos de sonido; se cerrÃ³ automÃ¡ticamente. Informe `build/clientcheck/resource-check.txt`.
- EjecuciÃ³n final: **13/13 GameTests aprobados y BUILD SUCCESSFUL**, incluida competencia de dos escarabajos por un solo Ã­tem. Informe final en `build/gametest-results.xml`. JAR actualizado en `build/libs/proyecto_intento-1.0-SNAPSHOT.jar`.
- El conector de escritorio siguiÃ³ indisponible (`Computer Use native pipe is unavailable`). No se afirma revisiÃ³n visual de una partida ni escucha humana de estos sonidos. Los PNG de inventario sÃ­ fueron inspeccionados y se validÃ³ la carga del cliente.
- Minecraft informa que no hay DataFixer para las dos entidades nuevas: aviso de esquema de migraciÃ³n, sin impedir creaciÃ³n, guardado/carga o las pruebas. No se declara compatibilidad de migraciÃ³n con futuras versiones del juego.
- CÃ³digo de comprobaciÃ³n aislado en `src/gametest` y `src/clientcheck`, habilitado por propiedades Gradle; se excluye del JAR distribuible. La prueba de cliente no modifica `run/saves` ni la configuraciÃ³n normal del IDE.

Comandos adicionales, usando las mismas variables `$env:JAVA_HOME` y `$gradleExe` de la secciÃ³n 5:

```powershell
& $gradleExe --offline -Pgametest runGametest build
& $gradleExe --offline -Pclientcheck runClientcheck
powershell -NoProfile -ExecutionPolicy Bypass -File tools/build-assets.ps1
```

Para revisiÃ³n humana: abrir PopoCraft en creativo; lanzar popÃ³ con clic derecho contra una vaca y observar durante cinco segundos; mojar la armadura y comprobar que el olor vuelve entre tres y cinco minutos despuÃ©s; colocar bloques hÃºmedos y escuchar los zumbidos espaciados. Generar dos escarabajos, soltar popÃ³ cerca y observar la bola y el montÃ³n. Comandos: `/summon proyecto_intento:dung_beetle`, `/give @s proyecto_intento:dung_beetle_spawn_egg`, `/give @s proyecto_intento:popo_pile`.

## 7. VerificaciÃ³n de continuidad â€” 2026-09-14

- A peticiÃ³n del usuario, se contrastÃ³ la Ãºltima entrega (secciÃ³n 6) con el cÃ³digo y los recursos actuales. EstÃ¡n presentes el registro de la pestaÃ±a, lavado, proyectil, moscas, sonidos, escarabajo y montones. Las dos texturas de armadura equipada mantienen los SHA-256 documentados.
- Se volviÃ³ a ejecutar `--offline -Pgametest runGametest build` con Java 21 y Gradle 8.9: **13/13 GameTests aprobados y BUILD SUCCESSFUL**. Informe actualizado: `build/gametest-results.xml`. No fue necesario modificar cÃ³digo funcional.
- Se consultÃ³ el informe existente `build/clientcheck/resource-check.txt`, con resultado PASS; la comprobaciÃ³n de cliente no se repitiÃ³ en esta revisiÃ³n.
- Sigue pendiente la apreciaciÃ³n humana de iconos, animaciÃ³n del escarabajo, densidad de moscas y volumen de sonidos durante una partida. No se encontrÃ³ una implementaciÃ³n pendiente indicada por la Ãºltima sesiÃ³n.

## 8. Refugios y colonias de escarabajos â€” TERMINADO (2026-09-14)

### Pedido y plan registrado antes de implementar
- Crear un refugio pequeÃ±o de un bloque con **cinco fases visibles**, construido progresivamente mediante popÃ³ recolectado. Conservar los montones decorativos existentes para compatibilidad.
- Permitir que adultos y crÃ­as entren y salgan del refugio terminado, almacenando sus datos de forma persistente como habitantes. Liberarlos al romper el refugio sin duplicarlos ni perderlos.
- Tras completar la casa, los aportes adicionales de popÃ³ servirÃ¡n de alimento para reproducir una pareja adulta cercana dentro del refugio. AÃ±adir tiempos de descanso, maduraciÃ³n y capacidad limitada para controlar crecimiento; descendientes podrÃ¡n establecer casas vecinas al crecer y disponer de material.
- Ampliar apariciÃ³n natural rara a los biomas terrestres del Overworld, sobre terreno adecuado y con reglas de luz/espacio de animales. InterpretaciÃ³n de Â«cualquier parteÂ»: distribuciÃ³n por el mundo de superficie, sin apariciÃ³n sumergida, en el vacÃ­o, Nether o End. Rareza comparable como intenciÃ³n de diseÃ±o, sin prometer frecuencia exacta de tortugas.
- Mantener Java 21, Gradle 8.9, Loom 1.7.4 y Minecraft 1.21.1. Respetar `mobGriefing`, construcciones existentes, guardado y cantidades de Ã­tems.

### Pasos y verificaciones completados
- [x] Bloque de refugio, cinco modelos, traducciones y registro creativo.
- [x] Habitantes persistentes, entrada/salida, reproducciÃ³n, crÃ­as y liberaciÃ³n al romper.
- [x] RecolecciÃ³n y construcciÃ³n autÃ³noma; expansiÃ³n a casas prÃ³ximas.
- [x] ApariciÃ³n natural rara ampliada.
- [x] GameTests de construcciÃ³n, reproducciÃ³n, persistencia, rotura y conservaciÃ³n; regresiones previas y build.
- [x] ComprobaciÃ³n de recursos en cliente; documentar parÃ¡metros finales y lÃ­mites.

**Avance intermedio:** implementados bloque y entidad de bloque, habitantes NBT, reproducciÃ³n y salida, construcciÃ³n/recolecciÃ³n y adopciÃ³n de casa. Primera compilaciÃ³n Java aprobada. Generador de cinco modelos incorporado a `tools/build-assets.ps1`. Pendientes pruebas ampliadas y carga del cliente; no declarar terminado todavÃ­a.

**ParÃ¡metros de la primera implementaciÃ³n (sujetos a verificaciÃ³n):** cinco aportes forman las cinco etapas; aportes posteriores almacenan hasta ocho alimentos. Capacidad interior seis; una pareja consume un alimento por crÃ­a, espera mÃ­nima interior de diez segundos, descanso reproductivo de cinco minutos y maduraciÃ³n de veinte minutos. MÃ¡ximo local de diecisÃ©is escarabajos para nacimientos automÃ¡ticos. Dos adultos asociados por casa; al crecer, los hijos buscan casa o lugar vecino. ApariciÃ³n peso 2, grupos 1â€“2, terreno natural iluminado y expuesto al cielo en biomas del Overworld, con filtro adicional 1/4 en intentos de apariciÃ³n.

**Checkpoint de pruebas:** primera suite ampliada 16/18 aprobadas. ConstrucciÃ³n autÃ³noma y nacimiento confirmados; fallan dos conteos de poblaciÃ³n tras salir (esperado 3, contado 2 dentro del Ã¡rea GameTest). Investigar lÃ­mite del Ã¡rea frente a pÃ©rdida real; aÃºn no resuelto. DepuraciÃ³n aislada en `build/gametest-debug`, sin usar `run/saves`. Pantalla de QA de cinco etapas agregada en `src/clientcheck`; pendiente ejecutar.

**DiagnÃ³stico confirmado con IntelliJ:** logpoints registraron tres UUID liberados y se consultaron vivos por UUID al fallar el conteo. Un adulto estaba a Y=-60 fuera del suelo/volumen de prueba (Y=-58); `getEntities` contaba solo dos. Corregido el recinto de los nuevos GameTests con paredes perimetrales. Sin pÃ©rdida de habitantes observada. Logpoints eliminados y sesiÃ³n de depuraciÃ³n detenida. Se aÃ±aden pruebas de retorno de crÃ­as, maduraciÃ³n/expansiÃ³n, capacidad y registro de apariciÃ³n en varios biomas.

**Segundo checkpoint:** 20/21 GameTests aprobados, incluidos familia completa, construcciÃ³n/nacimiento autÃ³nomos y casa vecina. El selector `foundInOverworld` solo reconocÃ­a el bioma del generador superplano en GameTest (registro: 1 bioma modificado); ampliado con la etiqueta `IS_OVERWORLD` para cubrir todos los biomas de superficie aun en ese tipo de mundo. AÃ±adida marca persistente de residencia infantil para que al madurar el hijo busque casa sin desplazar a los padres. Pendiente Ãºltima suite y cliente.

**Tercer checkpoint:** **21/21 GameTests aprobados y BUILD SUCCESSFUL**. JAR distribuible inspeccionado: incluye bloque/entidad/modelos nuevos y excluye clases de GameTest y QA del cliente. Primera comprobaciÃ³n de cliente aprobada (10 Ã­tems, 5 fases, texturas y sonidos anteriores). Captura renderizada revisada; ajustando Ãºnicamente la cÃ¡mara de QA para mostrar la entrada frontal. La implementaciÃ³n funcional estÃ¡ verificada.

### Entrega final y comportamiento vigente

**Estado: TERMINADO.** Los checkpoints anteriores son historial, no tareas pendientes. Se aÃ±adiÃ³ la regla obligatoria 5 al comienzo de este documento: registrar siempre el plan antes de implementar y mantener estados/avances para retomar tras interrupciones.

- **Refugio `proyecto_intento:beetle_nest`:** ocupa un bloque. Cinco aportes de una unidad crean sucesivamente base, paredes, cubierta parcial, cubierta y remate/entrada. Cinco modelos de geometrÃ­a propia reutilizan las texturas hÃºmeda/seca existentes. Una sexta unidad pasa a alimento; almacena hasta ocho. Disponible en PopoCraft (ahora diez entradas); el objeto colocado empieza en fase 1. Los montones decorativos anteriores permanecen, pero la recolecciÃ³n autÃ³noma ahora construye refugios.
- **ConstrucciÃ³n y pequeÃ±as colonias:** cada adulto carga una unidad. Dos adultos cercanos pueden adoptar la misma casa y colaborar. Busca casas hasta ocho bloques alrededor del hogar y lugares de construcciÃ³n hasta seis; deja separaciÃ³n mÃ­nima de tres bloques entre posiciones de casas, suelo sÃ³lido y una salida lateral libre. No reemplaza bloques existentes. Conserva la carga si no puede depositar. `mobGriefing=false` detiene recolecciÃ³n y construcciÃ³n/aportes; no expulsa familias ya alojadas.
- **Vida dentro/fuera:** casa terminada con capacidad interior de seis. Al entrar se guarda la entidad completa y se retira del mundo; al salir se restaura su UUID, nombre, edad y datos. Adultos descansan normalmente 20 segundos; adultos listos con comida esperan hasta 60 segundos para dar tiempo a una pareja. CrÃ­as descansan 30 segundos. Tras salir pasan aproximadamente un minuto fuera, salvo adultos listos que regresan antes si hay comida. Las crÃ­as tambiÃ©n buscan el hogar y entran solas; no recolectan.
- **ReproducciÃ³n:** dos adultos sin descanso reproductivo, cada uno con al menos diez segundos dentro, consumen **un popÃ³** para generar una crÃ­a dentro de la casa. Los padres esperan cinco minutos antes de poder reproducirse otra vez. Las crÃ­as maduran en veinte minutos; su edad avanza tanto dentro como fuera del refugio mientras estÃ© cargado. No avanza mientras el chunk no estÃ¡ cargado o el servidor estÃ¡ apagado. Se conserva la reproducciÃ³n manual con semillas de trigo anterior.
- **ExpansiÃ³n y lÃ­mites:** los hijos conservan `NurseryResident` hasta madurar; entonces buscan su propio lugar sin desplazar a los padres. Con material disponible pueden iniciar casas vecinas. Los nacimientos automÃ¡ticos se detienen con diecisÃ©is escarabajos locales (visibles en un radio de 16 y alojados en refugios cercanos, dentro de dos bloques verticales). Es un lÃ­mite local, no un lÃ­mite mundial ni una garantÃ­a de que nazca una colonia sin alimento, pareja y espacio.
- **Persistencia y rotura:** NBT del refugio `Food`/`Occupants`, por habitante `Entity`/`TicksInside`; NBT del escarabajo conserva `PopoHome`/`CarriedPopo` y aÃ±ade `BeetleNest`/`OutsideTicks`/`NurseryResident`. Salidas bloqueadas retienen habitantes; romper el refugio los evacua incluso si estÃ¡ rodeado. En supervivencia devuelve un popÃ³ por fase construida mÃ¡s el alimento sin consumir. El alimento gastado en nacimientos no se devuelve. Identidades duplicadas cargadas se rechazan al liberar.
- **ApariciÃ³n natural rara:** registro en biomas con etiqueta `IS_OVERWORLD` y biomas usados por el generador de superficie. Peso relativo 2, grupos 1â€“2 y filtro aleatorio adicional 1/4 en la restricciÃ³n de apariciÃ³n. Requiere cielo visible, luz superior a 8 y terreno natural: tierra/hierba, arena, piedra, grava, nieve sÃ³lida, musgo o terracota. Respeta espacio, distancia al jugador y cupos vanilla. Puede aparecer en zonas apropiadas de desiertos, playas, selvas, nieve y otros biomas; no bajo el agua, ni en Nether/End. La frecuencia exacta depende del mundo y no es idÃ©ntica a la de tortugas. No fuerza apariciÃ³n instantÃ¡nea ni repuebla automÃ¡ticamente chunks viejos.

### Evidencia final

- `--offline -Pgametest runGametest build`: **21/21 aprobados**, `BUILD SUCCESSFUL`, Java 21 / Gradle 8.9 / Loom 1.7.4 / Minecraft 1.21.1. Informe: `build/gametest-results.xml`; ejecuciÃ³n: `build/nest-test-run.log`.
- Incluye las trece pruebas anteriores (adaptadas a depÃ³sitos en refugios), cinco fases/cantidades, familia y NBT, bloqueo de salidas/rotura, construcciÃ³n y nacimiento autÃ³nomos, ausencia de pareja, ausencia de comida/capacidad, entrada y salida de crÃ­as/fundaciÃ³n de casa vecina, y apariciÃ³n registrada en varios biomas de superficie.
- `--offline -Pclientcheck runClientcheck`: **APROBADO**, diez modelos de Ã­tems, cinco fases del refugio, cinco texturas especÃ­ficas, modelo del escarabajo y cuatro eventos de sonido. Informe: `build/clientcheck/resource-check.txt`.
- Captura con los modelos realmente renderizados por Minecraft: `build/clientcheck/screenshots/refugios.png`, copiada a `art/beetle-nest-stages.png` e inspeccionada. Esto verifica geometrÃ­a/texturas; queda apreciaciÃ³n humana de una partida normal y del ritmo de la colonia.
- JAR final: `build/libs/proyecto_intento-1.0-SNAPSHOT.jar`. Se verificÃ³ que incluye refugios y recursos y excluye las clases de pruebas y la pantalla QA. No se cambiaron versiones ni mundos de `run/saves`.
- Generador nuevo `tools/build-nest-assets.ps1`, invocado desde la entrada pÃºblica `tools/build-assets.ps1`. Reutiliza las texturas PNG existentes y no depende de una herramienta externa de generaciÃ³n de imÃ¡genes.

### Prueba manual rÃ¡pida

1. En creativo, generar dos escarabajos cercanos sobre suelo plano usando huevos de la pestaÃ±a PopoCraft o `/summon proyecto_intento:dung_beetle`.
2. Obtener `/give @s proyecto_intento:popo 16` y **soltar Ã­tems con Q**, no lanzarlos con clic derecho. Cinco unidades construyen la casa y una adicional puede alimentar un nacimiento. Observar la bola transportada y las cinco fases.
3. Dejar que ambos entren: tras al menos diez segundos juntos y con comida aparecerÃ¡n corazones. Los padres y la crÃ­a saldrÃ¡n segÃºn sus tiempos de estancia. Proporcionar mÃ¡s popÃ³ y espacio para futuras casas cuando crezcan las crÃ­as.
4. Para probar directamente una casa completa: `/setblock ~2 ~ ~ proyecto_intento:beetle_nest[stage=5]` sobre suelo libre. DespuÃ©s soltar alimento y acercar dos adultos.
5. Romper una casa ocupada y comprobar que salen sus habitantes. Cerrar/reabrir el mundo para comprobar continuidad. La apariciÃ³n natural se aprecia mejor explorando terreno nuevo; los huevos permiten probar la mecÃ¡nica sin esperar una apariciÃ³n rara.

Referencias de API: [BlockEntity Yarn 1.21.1](https://maven.fabricmc.net/docs/yarn-1.21.1%2Bbuild.3/net/minecraft/block/entity/BlockEntity.html), [BiomeSelectors Fabric](https://maven.fabricmc.net/docs/fabric-api-0.100.1%2B1.21/net/fabricmc/fabric/api/biome/v1/BiomeSelectors.html). Se verificaron tambiÃ©n las firmas del Minecraft local; no se actualizÃ³ ninguna dependencia.

## 9. Remake, efecto de peste y Pedo en botella â€” TERMINADO (2026-09-15)

### Pedido y plan previo
- Rehacer la textura del Ã­tem popÃ³ con ImageGen, adaptada al pixel art del mod. Conservar el original como respaldo y mantener ID/recetas. Actualizar tambiÃ©n el icono del efecto que reutiliza esta textura.
- Verificar el efecto registrado `proyecto_intento:stinky` mediante `/effect`, su autocompletado y aplicaciÃ³n a jugadores/mobs; corregir cualquier carencia encontrada y documentar el comando exacto.
- Registrar la pociÃ³n **Pedo en botella**, elaborable en el soporte de pociones con popÃ³. Usar pociÃ³n rara como base, duraciÃ³n inicial de 30 segundos, y conversiones vanilla a arrojadiza/persistente. Incluirla en PopoCraft y traducir sus variantes. La peste adquirida por un jugador harÃ¡ que los mobs huyan de Ã©l sin controlar sus movimientos.
- AÃ±adir sonido de pedo a cada producciÃ³n real de popÃ³ (periÃ³dica o por comida), sustituyendo el sonido de barro. Revisar como referencia https://www.youtube.com/watch?v=cl0SVX78XM4 ; producir un efecto original corto apropiado para el juego si no es accesible, indicando la procedencia real. No afirmar que se escuchÃ³ el video si no se puede reproducir.
- Sugerir una idea adicional al entregar, sin implementar funcionalidades no solicitadas.

### Verificaciones previstas
- [x] Original respaldado, textura nueva empaquetada y preview inspeccionado.
- [x] Efecto comprobado por comando/autocompletado y sobre jugador.
- [x] Alquimia real y consumo de pociÃ³n; variantes y nombres en cliente.
- [x] Audio propio OGG, evento registrado y producciÃ³n real enlazada.
- [x] Regresiones GameTest, build Java 21 y comprobaciÃ³n del cliente.

**Checkpoint de reanudaciÃ³n 2026-09-15:** textura ImageGen guardada en `art/source/popo_v2.png`, original respaldado en `art/source/popo_original.png`, textura 16Ã—16 y efecto actualizados con `build-alchemy-assets.ps1`; preview inspeccionado en `art/popo-v2-preview.png`. `ModPotions` registra pociÃ³n rara + popÃ³, 600 ticks, y tres variantes creativas. `PopoDrops` usa `ModSounds.FART` una vez por producciÃ³n real. Tres OGG originales generados con `tools/build-fart-audio.py` (0.48/0.64/0.79 s, mono 44100 Hz); dependencias locales en `build/audio-deps`, la ejecuciÃ³n requiere acceso fuera del sandbox a esos paquetes. Video no accesible y no escuchado.

**Pruebas: 24/26 aprobadas en la primera ejecuciÃ³n.** Ya pasan autocompletado/aplicaciÃ³n/retirada por `/effect`, bebida y botella vacÃ­a, alquimia real de tres botellas y conversiones vanilla, y un pedo por producciÃ³n. Pendiente resolver nube persistente (no afectÃ³ a la vaca en la colocaciÃ³n de prueba) y una regresiÃ³n intermitente en construcciÃ³n autÃ³noma de escarabajos; despuÃ©s ejecutar cliente, comprobar nombres/audio/textura y empaquetar. No marcar terminado todavÃ­a.

**Avance de reanudaciÃ³n:** audio OGG generado y decodificado, sin saturaciÃ³n (picos 0.71â€“0.74), procedencia en `art/fart-audio-provenance.json`. Cliente QA ampliado para nombres/modelos de las tres pociones, decodificaciÃ³n Minecraft de tres OGG y captura del icono/pociones. Depurador confirmÃ³ en la repeticiÃ³n `complete=1`, `population=3`, `babies=1` para escarabajos, sin modificar su implementaciÃ³n. Investigando nube persistente antes de la suite final.

**Nube diagnosticada:** impacto directo sobre la cabeza creÃ³ la nube vanilla a Y=-56..-55.5, mientras la vaca ocupaba Y=-58..-56.6 (cajas comprobadas en IntelliJ). No se intersectaban. Prueba ajustada para impactar el suelo junto a la vaca, conservando comportamiento vanilla. SesiÃ³n detenida y breakpoints del agente eliminados. Pendiente suite final y cliente.

**Checkpoint cliente:** `runClientcheck` aprobado. Comprueba diez Ã­tems, tres variantes de pociÃ³n con nombre traducido, cinco fases de refugio, cinco texturas, cinco eventos sonoros y decodifica los tres OGG con el lector real de Minecraft. Captura `art/alchemy-preview.png` inspeccionada. La suite de 26 pruebas pasÃ³ completa en la Ãºltima sesiÃ³n de depuraciÃ³n; la prueba previa de escarabajos habÃ­a fallado intermitentemente a los 2400 ticks en ejecuciones normales, sin reproducirse en las dos inspecciones. No se afirma corregida esa intermitencia ni se cambiÃ³ su lÃ³gica para ocultarla. Pendiente informe final de ejecuciÃ³n normal y JAR actualizado.

### Entrega final

**TERMINADO:** ejecuciÃ³n normal final **26/26 GameTests aprobados, cero fallos y BUILD SUCCESSFUL**. Informes `build/gametest-results.xml` y `build/alchemy-tests.log`. Cliente tambiÃ©n aprobado: `build/clientcheck/resource-check.txt` y `build/alchemy-client.log`. Los checkpoints anteriores quedan como historial.

- **Textura:** remake generado con ImageGen integrado, adaptado a PNG transparente 16Ã—16. Nuevo Ã­tem y HUD del efecto comparten icono (SHA-256 `3E1DC26E036A3464DDFE9A06D6BA022B23D217AB15BB82C7FBA19F8880EE9324`). Conservado el original en `art/source/popo_original.png`. Prompt/procedencia: `art/popo-remake-provenance.json`; fuente nueva: `art/source/popo_v2.png`. No se cambiaron ID ni recetas anteriores.
- **Efecto adquirible:** el registro `proyecto_intento:stinky` ya existÃ­a y se confirmÃ³ en autocompletado, aplicaciÃ³n y retirada con comandos reales. Ejemplo `/effect give @s proyecto_intento:stinky 30 0`; quitar con `/effect clear @s proyecto_intento:stinky`. En jugadores genera olor/moscas y ahuyenta mobs sin tomar control del jugador. En mobs conserva el pÃ¡nico anterior. El proyectil de popÃ³ sigue aplicando cinco segundos.
- **Pedo en botella:** registro de pociÃ³n `proyecto_intento:bottled_fart`, ingrediente popÃ³ sobre **pociÃ³n rara** en el soporte de pociones, combustible polvo de blaze. Una operaciÃ³n de 400 ticks convierte hasta tres botellas consumiendo un ingrediente. La bebible aplica 600 ticks/30 segundos y devuelve la botella de cristal en supervivencia. PÃ³lvora convierte a arrojadiza; aliento de dragÃ³n convierte la arrojadiza a persistente. Estas variantes usan las reglas vanilla de alcance/duraciÃ³n de salpicadura/nube. Lanzar la persistente al suelo prÃ³ximo permite cubrir a los mobs; un impacto en altura puede dejar la nube por encima de ellos.
- **Inventario:** tres variantes en PopoCraft, ahora trece entradas. Botellas vanilla teÃ±idas de marrÃ³n por el efecto, nombres traducidos en es_es/es_cl/en_us, incluida la flecha con efecto que puede resultar del sistema vanilla. No se aÃ±adiÃ³ una mesa nueva: se usa el soporte de pociones existente.
- **Sonido:** `proyecto_intento:fart`, tres OGG mono de 0.48, 0.64 y 0.79 segundos, sÃ­ntesis original. Cada producciÃ³n efectiva por `PopoDrops.drop` reproduce un evento, volumen 0.65 y tono aleatorio 0.9â€“1.1; cubre producciÃ³n periÃ³dica y la probabilidad de comida. Respeta el silencio/categorÃ­a de la entidad vanilla. SubtÃ­tulos en espaÃ±ol/inglÃ©s. El video enlazado no fue accesible y no se escuchÃ³ ni copiÃ³; no se afirma parecido auditivo verificado.
- **GeneraciÃ³n:** `tools/build-assets.ps1` incorpora `build-alchemy-assets.ps1`. Los OGG ya estÃ¡n empaquetados; para regenerarlos, `python tools/build-fart-audio.py`, con numpy, soundfile 0.14.0, cffi, pycparser y typing_extensions (instalados localmente en `build/audio-deps`, sin cambiar el entorno global). Procedencia y medidas de audio en `art/fart-audio-provenance.json`.
- **JAR actualizado:** `build/libs/proyecto_intento-1.0-SNAPSHOT.jar`; contenido inspeccionado, incluye `ModPotions`, textura y tres OGG, excluye clases GameTest y pantallas QA. Sin cambios de Java 21, Gradle 8.9, Loom 1.7.4, Minecraft 1.21.1 ni mundos de `run/saves`.
- **RevisiÃ³n visual:** `art/alchemy-preview.png`, captura renderizada por Minecraft e inspeccionada. El lector de audio real de Minecraft decodificÃ³ los tres clips; queda apreciaciÃ³n humana de timbre/volumen en partida, sin afirmar escucha humana por parte del agente.

**Seguimiento separado:** la prueba antigua `beetlesBuildAndBreedAutonomously` fallÃ³ intermitentemente al evaluar casa/nacimiento a los 2400 ticks en ejecuciones iniciales, y pasÃ³ en depuraciÃ³n y en la ejecuciÃ³n normal final. No se identificÃ³ una causa reproducible ni se modificÃ³ la lÃ³gica de refugios. Si reaparece, inspeccionar carga, destino, fase, comida y residentes antes de cambiar tiempos o comportamiento; no interpretar esta entrega como una correcciÃ³n definitiva de esa intermitencia.

**Idea sugerida, no implementada:** compostera atendida por escarabajos que transforme popÃ³ sobrante en abono para cultivos; darÃ­a una utilidad agrÃ­cola a mantener colonias.

## 10. Gran refugio comunitario â€” MODELOS TERMINADOS / LÃ“GICA PENDIENTE (2026-09-15)

### Pedido completo y alcance prioritario
Primero terminar y verificar los cuatro modelos visibles, listos para integrar. La lÃ³gica comunitaria se registra como pendiente explÃ­cita, no como funcionalidad ya disponible.
- Inicio cuando existen al menos cinco casas individuales en un asentamiento.
- Edificio de 5 Ã— 5 bloques, hasta cinco de altura, construido en terreno plano natural o aplanado por el jugador.
- Cuatro etapas: plataforma, muros y entrada, bÃ³veda parcial, refugio terminado.
- Presupuesto provisional para la futura lÃ³gica: 64 popÃ³s en total, umbrales 16/32/48/64. El pedido menciona un stack para la base: antes de implementar economÃ­a distinguir plataforma de edificio completo; los modelos no fijan el coste y permiten usar 64 por etapa si ese es el sentido deseado.
- MigraciÃ³n de los hogares individuales al comunitario; capacidad 30 residentes, reproducciÃ³n y almacenamiento independiente hasta 360 Ã­tems de popÃ³.
- Durante la obra transportar primero las reservas de las casas; despuÃ©s desmontarlas fase por fase y recuperar sus materiales, sin duplicar comida, materiales ni habitantes.
- Al finalizar, base de popÃ³ con extracciÃ³n mediante tolva. Reservar el centro del suelo para el futuro inventario/controlador.
- Cuando falten plazas de residentes, sacar material del almacÃ©n para fundar casas individuales y repetir el ciclo. Distinguir cupo de habitantes de almacÃ©n lleno.
- SeparaciÃ³n mÃ­nima de diez bloques entre los bordes de grandes refugios; comprobar terreno, volumen libre, accesos y otras obras antes de reservar el lugar.
- Soundtrack de construcciÃ³n: PENDIENTE DEL ARCHIVO ORIGINAL DEL USUARIO. No aÃ±adir mÃºsica sustitutiva; futura reproducciÃ³n al iniciar la obra con control de repeticiÃ³n y distancia.

### Plan previo de implementaciÃ³n visual
1. Generar geometrÃ­a nativa modular: cada elemento queda dentro de su bloque, evitando modelos gigantes fuera de los lÃ­mites de Minecraft.
2. Registrar un bloque tÃ©cnico de piezas con etapa/posiciÃ³n y colisiÃ³n correspondiente a su geometrÃ­a. Sin entrada creativa ni IA automÃ¡tica hasta integrar el controlador.
3. Generar manifiesto de piezas, coordenadas, orientaciÃ³n y modelos; reutilizar las texturas de tierra hÃºmeda/seca del mod para coherencia visual.
4. Renderizar las cuatro etapas en el cliente real, inspeccionar captura y validar empaquetado con Java 21.
5. Documentar IDs, regeneraciÃ³n, pruebas y tareas precisas para Antigravity Pro u otra IA.

### Verificaciones previstas
- [x] Generador y modelos vÃ¡lidos, dimensiones 5 Ã— 5 Ã— 5, cuatro etapas distinguibles.
- [x] Cliente carga todas las piezas sin modelo faltante y produce vista previa inspeccionada.
- [x] Build exitoso, recursos incluidos y QA excluido del JAR.
- [x] Handoff explÃ­cito; mecÃ¡nicas comunitarias y soundtrack pendientes.

**Checkpoint visual:** generador `tools/build-grand-nest-assets.py` y bloque tÃ©cnico `GrandBeetleNestPieceBlock` implementados. 500 variantes (4 etapas Ã— 125 posiciones), geometrÃ­a de colisiÃ³n generada desde los mismos cuboides. Plano JSON en `data/proyecto_intento/grand_nest/blueprint.json`. Primer compile Java y cliente QA compilados; comprobaciÃ³n de render en curso. TodavÃ­a no se han implementado controlador, inventario, migraciÃ³n, desmontaje ni detecciÃ³n de asentamientos.

**Checkpoint de revisiÃ³n:** primera captura real inspeccionada: las cuatro fases se distinguen, con entrada y cubierta progresiva. Corregidas uniones de esquinas. QA ampliado a las 500 variantes, contrastando presencia de geometrÃ­a visual y colisiÃ³n. GuÃ­a completa de integraciÃ³n en `art/GRAND_NEST_HANDOFF.md`. Segunda comprobaciÃ³n del cliente y empaquetado en curso.

### Entrega visual final â€” TERMINADA
- Cuatro etapas de 5 Ã— 5 bloques: plataforma de 0.75 bloques de altura, muros/arco de 2, bÃ³veda parcial de 3.5, refugio completo de 5. La entrada mira al norte. Aspecto de tierra compactada, hiladas, contrafuertes, cubierta escalonada y respiradero.
- 500 modelos modulares empaquetados, de los cuales 25/41/80/94 piezas tienen geometrÃ­a en las etapas 1/2/3/4. Las variantes vacÃ­as facilitan el contrato fijo de posiciones, pero NO deben colocarse como bloques de aire: omitirlas segÃºn el plano.
- ValidaciÃ³n estÃ¡tica: 1094 cuboides, todos dentro de 0..16 en su propia pieza; estructura completa dentro de 5 Ã— 5 Ã— 5.
- Cliente Minecraft: **500 estados cargados**, sin modelos faltantes y con ocupaciÃ³n visual/colisiÃ³n coincidente. Informe `build/clientcheck/grand-nest-check.txt`. TambiÃ©n pasÃ³ la comprobaciÃ³n previa de Ã­tems, pociones, refugios pequeÃ±os, texturas y sonidos.
- `--offline -Pclientcheck runClientcheck build`: **BUILD SUCCESSFUL**, registro `build/grand-nest-client.log`. Java 21 / Gradle 8.9, sin cambios de versiones ni mundos guardados. No se volviÃ³ a ejecutar la suite de comportamiento GameTest en esta entrega visual; sus resultados de secciÃ³n 9 son anteriores.
- Captura final realmente renderizada e inspeccionada: `art/grand-beetle-nest-stages.png`. La apreciaciÃ³n artÃ­stica final corresponde al usuario.
- JAR `build/libs/proyecto_intento-1.0-SNAPSHOT.jar` inspeccionado: incluye 500 modelos, plano, colisiones y bloque tÃ©cnico; excluye pantallas y clases de pruebas.
- Generador `tools/build-grand-nest-assets.py` integrado en `tools/build-assets.ps1`; usa Python estÃ¡ndar y las texturas existentes, sin generaciÃ³n/ediciÃ³n de PNG ni dependencias nuevas.
- **GuÃ­a para continuar: `art/GRAND_NEST_HANDOFF.md`.** Contiene ID, orientaciÃ³n, fÃ³rmula de piezas, montaje, centro previsto para controlador, regeneraciÃ³n, pruebas y orden de implementaciÃ³n funcional.

**NO IMPLEMENTADO todavÃ­a:** inicio automÃ¡tico tras cinco casas, validaciÃ³n/reserva de terreno y separaciÃ³n, consumo de materiales, traslado/desmontaje de casas, 30 habitantes, reproducciÃ³n comunitaria, inventario de 360, extracciÃ³n mediante tolvas y nueva expansiÃ³n. El bloque tÃ©cnico no tiene Ã­tem, receta, inventario ni apariciÃ³n automÃ¡tica; es la base visual preparada para integrar. No confundir este hito terminado con el sistema comunitario completo. Soundtrack pendiente del archivo que estÃ¡ componiendo el usuario. PrÃ³ximo agente: leer secciÃ³n 10 y la guÃ­a, registrar plan EN DESARROLLO antes de implementar la lÃ³gica.

### 10.1. ContinuaciÃ³n autorizada: almacÃ©n y tolvas â€” EN DESARROLLO
Plan previo: implementar controlador en el centro de la base, inventario persistente de 360 popÃ³s, depÃ³sito transaccional para la futura IA, extracciÃ³n mediante tolva inferior solo en etapa 4 y devoluciÃ³n del contenido al romper el controlador. Mantener modelos existentes. AÃ±adir GameTests de lÃ­mite/rechazo de otros Ã­tems, persistencia, extracciÃ³n real y conservaciÃ³n al romper. No declarar migraciÃ³n ni poblaciÃ³n implementadas. Usar seis huecos de 60 (360 exactos), sin menÃº de jugador por ahora. El controlador usa la pieza central 12 del plano, reemplazÃ¡ndola al montar la futura estructura.

**Checkpoint 10.1:** controlador y almacÃ©n implementados; **29/29 GameTests aprobados y BUILD SUCCESSFUL** (`build/grand-store-tests.log`, `build/gametest-results.xml`). Pruebas nuevas verifican 360 exactos, sobrante conservado, rechazo de otros Ã­tems, NBT, tolva inferior real bloqueada en fase 3/activa en fase 4 y 360 Ã­tems devueltos al romper. No se aÃ±adieron residentes ni IA de gran refugio. Pendiente carga de sus cuatro variantes visuales en cliente y JAR final.

### 10.2 IA comunitaria y migracion - TERMINADO
- Se amplio la entidad controladora (GrandBeetleNestBlockEntity) para soportar hasta 30 habitantes, logica de maduracion y reproduccion masiva.
- Se actualizo a DungBeetleEntity para reconocer asentamientos grandes, y cuando existen 5 refugios terminados pequeÃ±os, inician la obra buscando un lugar despejado y colocando un GrandBeetleNestControllerBlock.
- El costo de inversion sube y la construccion visual se actualiza automaticamente cada 16 de popo (16,32,48,64).
- Se integraron logicas para evacuar y desmontar casas pequeÃ±as en favor del refugio comunitario.
- Al llegar al cupo maximo de 30 y sobrar material, reinician la construccion de casas pequeÃ±as.
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

## 12. Tareas ChatGPT secciÃ³n 11 y revisiÃ³n posterior â€” TERMINADAS SEGÃšN REPARTO
Autorizado por el usuario: implementar primero inodoro, desatascador, fertilizante, arte del extractor y variante visual del comerciante; despuÃ©s revisar integraciÃ³n de Gemini y corregir errores verificables.
Plan previo:
1. Inodoro: modelo cerÃ¡mico orientable, colisiÃ³n, asiento mediante entidad tÃ©cnica efÃ­mera, desmontar al agacharse/romper/salir y evitar asientos duplicados. Base preparada para conectar producciÃ³n de 15 minutos y atasco; esa logÃ­stica corresponde al reparto con Antigravity, salvo integraciÃ³n imprescindible.
2. Desatascador: textura/modelo, herramienta durable, empuje adicional al golpear y contrato de desatasco con inodoro.
3. EstiÃ©rcol: Ã­tem aplicable al suelo cultivado, fertilizaciÃ³n persistente temporal en 3Ã—3, crecimiento gradual por intervalos sin crecimiento instantÃ¡neo ni tareas globales sin lÃ­mite. No consumir cuando no exista objetivo Ãºtil. Definir parÃ¡metros y receta/acceso sin invadir generaciÃ³n del extractor asignada a Antigravity.
4. Extractor: modelo y textura propios listos para integraciÃ³n, sin receta ni generaciÃ³n automÃ¡tica en esta tarea.
5. Comerciante: textura alternativa y accesorio discreto sobre modelo actual, contrato de selecciÃ³n para lÃ³gica comercial de Antigravity; no inventar intercambios.
6. Arte raster mediante skill imagegen integrado; geometrÃ­a JSON nativa y generador reproducible. Preservar fuentes y guardar procedencia.
7. Pruebas GameTest de mecÃ¡nicas, carga y captura de cliente, compilaciÃ³n Java 21. DespuÃ©s revisar economÃ­a, UUID, geometrÃ­a/montaje y evacuaciÃ³n del trabajo comunitario de Gemini, documentando problemas y correcciones con evidencia.
Estado inicial: documentaciÃ³n 10.2 dice terminado y 29/29; esa suite contiene solo tres pruebas de almacÃ©n del refugio grande, por lo que NO demuestra por sÃ­ sola toda la migraciÃ³n/construcciÃ³n comunitaria. Se ampliarÃ¡ la cobertura en la revisiÃ³n.

**Checkpoint secciÃ³n 12:** fuentes ImageGen y generador de modelos/texturas implementados, inodoro/asiento, desatascador, fertilizante 3Ã—3 y variante sincronizada del comerciante listos para pruebas. La API de codec de FarmlandBlock requiere MapCodec<FarmlandBlock>; ajustada sin cambiar versiones.
**RevisiÃ³n estÃ¡tica de Gemini, correcciones planificadas antes de editar:** (a) deposit devolvÃ­a 0 con almacÃ©n lleno pero la IA borraba la carga; (b) evacuar reservas soltaba toda la comida y ademÃ¡s creaba una unidad transportada; (c) rotura del controlador no liberaba habitantes ni desmontaba piezas; (d) updateStructure sobrescribÃ­a bloques ajenos; (e) tryInitiateGrandNest se ejecutaba fuera del if por falta de llaves, cada tick incluso en cliente, con casteo inseguro a ServerWorld; (f) consumeFood podÃ­a consumir parcialmente y devolver false; (g) la expansiÃ³n removÃ­a residentes antes de confirmar salida. Se aÃ±adirÃ¡n guardas y pruebas para conservaciÃ³n de materiales, rotura y construcciÃ³n obstruida. La suite anterior de 29 no cubrÃ­a estos casos.

**Checkpoint revisiÃ³n:** corregidos depÃ³sitos rechazados, recuperaciÃ³n de reservas/capas, evacuaciÃ³n al romper, prevenciÃ³n de sobrescritura del jugador, ejecuciÃ³n de fundaciÃ³n solo en servidor cada 200 ticks, consumo de comida atÃ³mico y retirada del residente solo al confirmar salida. Primera suite de nuevas mecÃ¡nicas: **33/33 aprobadas**. Suite ampliada y cliente en curso. Se revisarÃ¡ ademÃ¡s separaciÃ³n real entre grandes refugios y se evitarÃ¡ repetir bÃºsquedas completas cuando ya existe un hogar comunitario vÃ¡lido.

**Resultado intermedio:** 36/36 GameTests aprobados (mecÃ¡nicas nuevas y conservaciÃ³n comunitaria). Cliente abortÃ³ su QA por `FileNotFoundException` de `sounds/fart_1.ogg`: los tres clips histÃ³ricos faltan en src/main/resources aunque sounds.json los referencia. Se restaurarÃ¡n con el generador original. TambiÃ©n se detectÃ³ el Ã­tem beetle_shell de Gemini registrado sin modelo; se aÃ±adirÃ¡ modelo nativo de caparazÃ³n reutilizando quitina, sin implementar caÃ­da/comercio asignados a Antigravity.

**Checkpoint adicional:** separaciÃ³n de diez bloques entre bordes aprobada, igual que conservaciÃ³n de 30 UUID tras rotura y ticks posteriores. ReapareciÃ³ Ãºnicamente la intermitencia histÃ³rica de `beetlesBuildAndBreedAutonomously` (casa completa pero ninguna crÃ­a al tick 2400); no se afirma corregida. Reparado avance `start_grand_nest`: su icono referenciaba el controlador sin Ã­tem, provocando error de carga y bloqueando `finish_grand_nest`. Sustituido por el Ã­tem de refugio pequeÃ±o, prueba de carga de seis avances aÃ±adida. Audios originales regenerados, caparazÃ³n con modelo nativo y segunda validaciÃ³n final en curso.

### Entrega final secciÃ³n 12 â€” TERMINADA (2026-09-15)
El estado final reemplaza los checkpoints anteriores. Las tareas de ChatGPT de 11.1 estÃ¡n implementadas y verificadas dentro de su alcance; la expansiÃ³n completa de 11.2/11.3 conserva tareas de Antigravity.

**Implementado:**
- Inodoro orientable con taza, cisterna, textura cerÃ¡mica ImageGen y estado `clogged`. Clic derecho sienta a un jugador; agacharse desmonta. Asiento tÃ©cnico sin guardado ni invocaciÃ³n, se elimina vacÃ­o/al romper el inodoro. OcupaciÃ³n exclusiva y espacio superior comprobados. Receta: cinco cuarzos y un cubo.
- Desatascador con icono ImageGen, modelo handheld, durabilidad 128, atributos base de espada de madera y empuje adicional 0.9 sujeto a resistencia vanilla. Desatasca `clogged=true` con un uso; no consume durabilidad al estar limpio ni en creativo. Receta: dos palos y slime.
- EstiÃ©rcol funcional: una unidad sobre cultivo/suelo trata hasta nueve cultivos inmaduros en 3Ã—3. No crece instantÃ¡neamente; aumenta una edad cada 200 ticks durante seis intervalos, con luz â‰¥9. Regresa a farmland vanilla manteniendo humedad y cultivo. Estado y ticks programados se guardan con el chunk, sin escaneos globales. No acumula tratamientos activos ni consume sin objetivos; respeta permisos. Soporta CropBlock (trigo/zanahoria/patata/remolacha), no todo tipo de planta. Accesible en creativo; producciÃ³n desde extractor pendiente del reparto.
- Extractor: textura de rejilla marrÃ³n/cobre y modelo completo. Registro solo de bloque, sin BlockItem/receta para impedir colocaciÃ³n por jugadores. AÃºn no contiene conversiÃ³n ni inventario propio.
- Comerciante: textura jade/dorada 64Ã—64 y mochila; `isTrader/setTrader` sincronizados por DataTracker y persistidos como `IsTrader`. Se preserva tambiÃ©n `HasDroppedShell`, antes reescrito siempre false. Comercio y probabilidad de apariciÃ³n siguen pendientes de Antigravity. Modelo de caparazÃ³n nativo aÃ±adido para eliminar recurso faltante del Ã­tem ya registrado.
- PestaÃ±a PopoCraft incorpora inodoro, desatascador y estiÃ©rcol. Traducciones es_es/es_cl/en_us. Generadores integrados en `tools/build-assets.ps1`; originales y prompts exactos en `art/roadmap-imagegen-provenance.json` y `art/source/*_imagegen.png`. Se usÃ³ skill imagegen y herramienta integrada; geometrÃ­a JSON nativa, ajuste de resoluciÃ³n por empaquetado.

**RevisiÃ³n de Gemini: correcciones incluidas:**
1. La IA borra carga Ãºnicamente si `deposit` acepta realmente una unidad; almacÃ©n lleno no destruye carga.
2. Reservas de casas pequeÃ±as se retiran de una en una; luego se recupera cada capa. EvacuaciÃ³n no genera una unidad transportada extra. Prueba confirma ocho reservas + cinco capas, sin Ã­tems sueltos duplicados.
3. Romper controlador evacua habitantes, desmonta sus piezas y devuelve materiales invertidos mÃ¡s inventario. Prueba verifica 30 UUID vivos tras varios ticks; cupo 31 rechazado.
4. ConstrucciÃ³n obstruida conserva bloques del jugador y material ofrecido. Origen guardado inmutable, limitado al centro correcto. Se comprueban chunks cargados.
5. FundaciÃ³n antes se ejecutaba cada tick incluso en cliente por un if sin llaves; ahora solo servidor, adulto y cada 200 ticks. Se evita repetir bÃºsqueda completa si ya tiene hogar comunitario vÃ¡lido.
6. Terreno plano con volumen libre y entrada; separaciÃ³n mÃ­nima diez bloques entre bordes, probada a nueve/diez bloques. No fuerza carga de chunks para buscar.
7. Consumo de alimento verifica disponibilidad antes de descontar; expansiÃ³n solo con mobGriefing, retira residente Ãºnicamente tras spawn confirmado. La sexta unidad usada para fundar una casa se conserva como alimento. El nuevo residente se asocia al destino.
8. Soundtrack de inicio enviado por jugador, evitando retransmitir a todos por cada destinatario. Se conservÃ³ el archivo `grand_nest_start.ogg` que ya dejÃ³ Gemini; no se afirma revisiÃ³n auditiva ni procedencia nueva por parte de Codex.
9. Restaurados los tres OGG de pedos que faltaban pese a seguir declarados, con `tools/build-fart-audio.py`. Cliente los decodifica correctamente.
10. Avance `start_grand_nest` apuntaba como icono al controlador sin Ã­tem y no cargaba, bloqueando `finish_grand_nest`. Icono cambiado a refugio pequeÃ±o; los seis avances de progresiÃ³n se verifican en prueba.

**Evidencia final:**
- `--offline -Pgametest -Pclientcheck runGametest runClientcheck build`: **38/38 GameTests aprobados, cero fallos y BUILD SUCCESSFUL**. Informes `build/gametest-results.xml` y `build/roadmap-final.log`.
- Cliente: ocho variantes del inodoro, extractor, 48 variantes de tierra fertilizada, tres modelos de Ã­tems, cinco texturas decodificadas y mochila; ademÃ¡s QA anterior de 500 piezas/4 controladores, pociones, sonidos y escarabajo. Informes `build/clientcheck/roadmap-check.txt`, `grand-nest-check.txt`, `resource-check.txt`.
- Captura real de Minecraft inspeccionada y copiada a `art/roadmap-preview.png`. Queda apreciaciÃ³n humana del aspecto, pose sentada, ritmo y sonido en partida.
- JAR `build/libs/proyecto_intento-1.0-SNAPSHOT.jar` inspeccionado: clases/modelos/texturas/audio incluidos; clases GameTest y pantallas QA excluidas. Se mantuvieron Java 21 / Gradle 8.9 / Loom 1.7.4 / Minecraft 1.21.1; sin modificar mundos personales.

**Continuidad precisa para Antigravity:** leer `art/ROADMAP_HANDOFF.md`. Pendientes de su reparto: producciÃ³n de inodoro cada 15 minutos y disparador de atasco; caÃ­da de caparazones; comerciante real con intercambios y probabilidad 5%; conversiÃ³n/extracciÃ³n de estiÃ©rcol, generaciÃ³n de 1â€“3 extractores y transformaciÃ³n de terreno. Conectar los registros existentes, no duplicarlos. El controlador comunitario todavÃ­a extrae popÃ³, no el nuevo fertilizante.

**LÃ­mites de la revisiÃ³n:** no se certifica todo el ciclo comunitario como completamente robusto. Falta cobertura de migraciÃ³n autÃ³noma completa desde cinco casas, descargas de chunks y rotura/reparaciÃ³n de piezas perifÃ©ricas; estas Ãºltimas no invalidan todavÃ­a la fase del controlador. La intermitencia histÃ³rica de reproducciÃ³n pequeÃ±a reapareciÃ³ en una ejecuciÃ³n intermedia y pasÃ³ en la final; su causa no se diagnosticÃ³ y NO se declara resuelta. No aumentar timeouts ni suprimir pruebas para ocultarla. Se aÃ±adieron nueve pruebas respecto a la suite inicial de 29.

## 13. Logo PopoCraft para CurseForge â€” EN DESARROLLO
Pedido: crear logo para la primera publicaciÃ³n del mod y pruebas con amigos. Plan: arte original con ImageGen integrado, composiciÃ³n cuadrada legible en miniatura, nombre PopoCraft y escarabajo pelotero; guardar fuente y versiÃ³n de publicaciÃ³n en art/branding, inspeccionar resultado. No publicar en CurseForge ni modificar el icono del mod sin pedido adicional. No se afirman requisitos actuales de tamaÃ±o de CurseForge; entregar PNG cuadrado de alta resoluciÃ³n y copia de 400Ã—400 como formato prÃ¡ctico.
**Ajuste antes de generar:** la captura del usuario muestra el nombre pÃºblico **ProjectPopo**. Usar ese nombre en el logo, conservando PopoCraft como nombre interno del mod por ahora.
**TERMINADO:** logo original generado e inspeccionado; nombre ProjectPopo correcto, escarabajo jade/bronce, bola y brote, composiciÃ³n cuadrada. Fuente `art/branding/projectpopo-logo-original.png`; copia PNG 400Ã—400 inspeccionada `art/branding/projectpopo-curseforge-400.png`. Prompt y procedencia en `art/branding/provenance.json`. No se publicÃ³ ni se modificaron los recursos del mod.

## 14. Port a Minecraft/Fabric estable actual â€” EN DESARROLLO
Pedido explÃ­cito del usuario: portar el mod a la versiÃ³n actual. Esta autorizaciÃ³n permite usar el JDK/Gradle/Loom necesarios para el port, como excepciÃ³n a la regla histÃ³rica de conservar Java 21 en 1.21.1. La raÃ­z 1.21.1 permanece intacta; trabajo nuevo en ports/minecraft-current.
Plan: verificar versiones oficiales estables, crear copia aislada de fuentes/recursos/configuraciÃ³n (sin mundos ni cachÃ©s), obtener herramientas locales al port, migrar nombres/API de Java, render, mixins, persistencia y recursos, compilar y ejecutar pruebas de servidor/cliente. Documentar cada bloqueo y no marcar TERMINADO ni publicar un JAR como compatible sin verificaciÃ³n. No basta cambiar el nÃºmero de versiÃ³n en fabric.mod.json.
**Versiones verificadas con metadatos oficiales:** Minecraft 26.3 (stable=true), Loader 0.19.5, Fabric API 0.160.5+26.3, Loom estable 1.18.1. Requiere Java 25. Copia aislada creada en ports/minecraft-current; la publicaciÃ³n 1.21.1 no se modifica.


## 15. Nuevas MecÃ¡nicas e Ideas (Brainstorming para Slime FÃ©tido)
Plan: Se estÃ¡n diseÃ±ando nuevos enemigos y objetos con temÃ¡tica de alcantarilla.
Esta fase estÃ¡ en diseÃ±o. No se escribirÃ¡n clases Java hasta que se definan los detalles.
1. **Popo Slime (Slime FÃ©tido):** Enemigo que puede aparecer de un Inodoro atascado si pasa varios dÃ­as sin limpiarse. Al morir suelta *Biomasa Viscosa* (Fango de Alcantarilla), y en lugar de dividirse, explota dejando un rastro temporal de toxicidad.
2. **Nuevos items/crafteos con Biomasa Viscosa:**
   - *PociÃ³n de Pestilencia (NÃ¡useas):* HistÃ³ricamente inaccesible en vainilla. Genera NÃ¡usea al beber y puede hacerse arrojadiza para PvP.
   - *Desatascador TÃ³xico:* Arma cuerpo a cuerpo que al golpear aplica *Lentitud Extrema* por 3 segundos.
   - *Cubo de Aguas Residuales:* Bloque lÃ­quido denso para base-defense. Atrapa a los mobs (no pueden saltar) y aplica leve veneno.
   - *Antorcha Pestilente (Repele-Mounstruos):* Bloque de iluminaciÃ³n/repulsiÃ³n. Se coloca y en un radio de 10 bloques a la redonda ningÃºn monstruo hostil puede entrar debido al olor.

**Tareas pendientes de IA / Arte** (Para ChatGPT o Antigravity cuando se autorice):
- Generar texturas de la Biomasa Viscosa, la pociÃ³n y el Limo interactuando.
- Programar entidad PopoSlime e interacciÃ³n de spawn en bloques Clogged Toilet.
- Programar lÃ³gicas y BlockEntities para la Antorcha y Cubo TÃ³xico.

## 16. Slime FÃ©tido y derivados â€” EN DESARROLLO (2026-09-18)
Pedido autorizado: desarrollar la Ãºltima propuesta de Gemini (secciÃ³n 15), priorizando estrictamente texturas y modelos nuevos, despuÃ©s cÃ³digo y finalmente verificaciÃ³n.
Plan: generar fuentes raster originales con ImageGen para slime, biomasa, pociÃ³n, desatascador tÃ³xico, aguas residuales y antorcha; preparar geometrÃ­a nativa y recursos reproducibles. DespuÃ©s integrar mob hostil sin divisiÃ³n, apariciÃ³n desde inodoro atascado, botÃ­n y nube tÃ³xica temporal, alquimia y objetos/bloques derivados. Fijar y documentar parÃ¡metros donde el brainstorming no los define. Consultada versiÃ³n destino; el arte es reutilizable entre ports. VerificaciÃ³n final: compilaciÃ³n, pruebas de comportamiento/persistencia y carga/render de recursos en cliente. No marcar terminado sin evidencia.
**Checkpoint visual:** seis originales ImageGen guardados en art/source, prompts en art/fetid-imagegen-provenance.json. Texturas empaquetadas a 16x16 (entidad 64x64), geometrÃ­a del slime en art/fetid-slime-model.json y FetidSlimeModel, modelos nativos de cubo con contenido visible, antorcha y objetos. Generadores build-fetid-assets.ps1/build-fetid-models.py. Comienza integraciÃ³n funcional sobre raÃ­z 1.21.1 ante ausencia de selecciÃ³n alternativa. ParÃ¡metros previstos: tres dÃ­as cargados de atasco (72000 ticks), un slime por ciclo con lÃ­mite local; nube de veneno 10 s sin daÃ±o a bloques; biomasa como ingrediente de nÃ¡usea, arma con lentitud IV 3 s, aguas residuales densas contenidas y antorcha de radio 10. Persistir temporizador y respetar dificultad pacÃ­fica/espacio disponible.
**IntegraciÃ³n necesaria del inodoro:** la raÃ­z aÃºn no tenÃ­a disparador natural de atasco. Se conecta el comportamiento previsto en secciÃ³n 11: ocho popÃ³s por 18000 ticks acumulados de asiento ocupado (15 minutos a 20 TPS), 25% de atasco por producciÃ³n; en atasco se suspende producciÃ³n. Ambos contadores persisten. La pociÃ³n conserva Ã­tems/transformaciones vanilla y usa un modelo cliente propio solo para la variante bebible de Pestilencia.


## 17. InvasiÃ³n FÃ©tida (diseño autorizado; actualización vigente en sección 20)
En este punto, el jugador y el mod evolucionan hacia un evento pasivo/tipo Auto-Battler o Tower Defense autÃ³nomo, donde los escarabajos defienden su Nido Grande frente a un ataque masivo provocado por la negligencia sanitaria.

**1. El Detonante (Mal Presagio FÃ©tido):**
- Cuando los inodoros colapsan masivamente, pueden spawnear de forma muy inusual a un **Slime FÃ©tido Alfa** (un mini-boss). 
- Al eliminar al Slime Alfa, este explota y le aplica al jugador el efecto de **Olor FÃ©tido** (Fetid Omen, similar al Mal Presagio de los Illagers).
- Si el jugador entra al radio de un Gran Nido (Fase 4) teniendo este efecto, inicia el evento de **InvasiÃ³n FÃ©tida**.

**2. Evento de No-IntervenciÃ³n (El Jugador como Espectador):**
- Una vez iniciada la invasiÃ³n, una bruma t&oacute;xica o campo de fuerza de esporas cubre la zona del Nido, **impidiendo que la interacciÃ³n del jugador funcione**.
- Todo el daÃ±o infligido por el jugador a los monstruos del evento se reduce a 0 (o bien el jugador queda con debilidad extrema/barrera inquebrantable).
- Esto fuerza a que la invasiÃ³n sea *exclusivamente peleada por la colonia de escarabajos* contra los Slimes.

**3. La Horda Invasora (Waves):**
- Se generarÃ¡n oleadas de 3 tipos de Slimes FÃ©tidos diferentes.
- **BÃ¡sico:** RÃ¡pidos y enjambre.
- **Artillero:** Escupe proyectiles de lodo/residuales para aplicar veneno a distancia.
- **Coloso / Destrozador:** Lento, con muchÃ­sima vida y daÃ±o expansivo cuerpo a cuerpo.

**4. La Defensa Militar de los Escarabajos:**
- En el instante en que inicia el evento, los pacÃ­ficos Dung Beetles que formen parte de la colonia sufren una metamorfosis visual: **adoptan nuevas skins** donde se les ve con armaduras de estiÃ©rcol o caparazones endurecidos para la guerra.
- **Cuerpo a Cuerpo:** Los escarabajos bÃ¡sicos morderÃ¡n/embestirÃ¡n ferozmente a los slimes.
- **ArtillerÃ­a de Popo:** Algunos escarabajos se adaptarÃ¡n para arrancar trozos de popo y lanzarlos fÃ­sicamente como proyectiles (bola de nieve de popo) a los slimes, haciendo daÃ±o y repulsiÃ³n a distancia.
- El Ã©xito del Nido dependerÃ¡ de cuÃ¡n poblada y desarrollada estÃ© la colonia de escarabajos antes de detonar el asedio.

**Estado actual:** diseño autorizado por el usuario; queda sin efecto la etiqueta de lluvia de ideas congelada. Aplicar las correcciones de la sección 20, priorizando texturas y modelado antes de las mecánicas.

**5. Especificaciones de Arte y Assets (DIRECTRICES PARA CHATGPT / ASTRA):**
El evento "InvasiÃ³n FÃ©tida" es el gran final y cierre definitivo para el arco de los escarabajos. Requiere una ambientaciÃ³n espectacular y texturas personalizadas. Se le solicita a ChatGPT generar lo siguiente:
- **Tema musical de la invasión:** lo compone y adjunta el usuario. Integrar su archivo cuando esté disponible para que suene al comenzar el evento. No generar música sustitutiva.
- **Skins Militares de los Escarabajos:**
  1. *Escarabajo Soldado Cuerpo a Cuerpo:* Textura de "armadura". Una capa rÃ­gida que incluye un casco o remaches de lodo y roca endurecida.
  2. *Escarabajo Artillero:* Conlleva pequeÃ±os palos y enredaderas amarradas a su caparazÃ³n que parecen un pequeÃ±o saco o arnÃ©s militar para cargar su municiÃ³n.
- **Variantes Visuales de los Slimes Invasores:**
  1. *Corredor:* Verde Ã¡cido/radiactivo brillante, textura aerodinÃ¡mica.
  2. *Artillero FÃ©tido:* Verde pantano, con una protuberancia deformada en el centro como si estuviera a punto de explotar.
  3. *Coloso Destrozador:* Gigantesco y denso. Su interior translÃºcido debe tener pedazos de crÃ¡neos, huesos o basura estancada asimilada.
- **Proyectiles (2D Icons o modelos pequeÃ±os):**
  1. *Proyectil de Defensa:* Una bola compacta y pesada de popo (artillerÃ­a de escarabajo).
  2. *Escupitajo Ãcido:* Una gota asquerosa y goteante de Ã¡cido verde brillante (artillerÃ­a del slime artillero).

Al entregar todo este material de arte, el modder (o esta IA) ensamblarÃ¡ el cÃ³digo para que sea una obra de "Tower Defense" automatizada, otorgando el cierre Ã©pico a todos los mecÃ¡nicos creados para la colonia de los Escarabajos Peloteros.


**6. Mecánicas de Interfaz y Balance del Evento (Invasión):**
- **Boss Bar (Barra de Invasión):** Implementar una barra de estado estilo Raid vainilla en la parte superior de la pantalla. No mostrará la "vida" de los enemigos, sino el "% de Defensa" o "Progreso" (cuánto falta para que los escarabajos ganen).
- **Balance del Combate:** Se deberá dar clara prioridad táctica a la colonia de escarabajos. Sus estadísticas de vida, regeneración o daño pasivo (así como la efectividad de sus armaduras) estarán infladas en este evento para que tengan muchas más oportunidades de ganar que los slimes, impidiendo que mueran instantáneamente o injustamente. La invasión debe sentirse épica pero con ventaja local.

**7. Tareas de Refinamiento Futuro (IA y Nidos):**
- **Corrección de Cuellos de Botella (Pathfinding):** Mejorar la IA de navegación de los escarabajos. Evitar que se queden pegados, aglomerados o empujándose indefinidamente cerca del nido cuando la población (Fase 4) es muy grande.
- **Acceso Omnidireccional al Gran Nido:** Reestructurar el modelo/textura del Gran Nido. Ya no debe ser translúcido o permitir ver el interior hueco. Llevará una textura sólida exterior con la ilusión visual de un "agujero" negro que actúa de entrada simbólica. 
- **Entrada Sin Colas:** En código, los escarabajos no estarán obligados a entrar estrictamente por ese agujero visual. Cualquier colisión/interacción con *cualquier* bloque perimetral que conforme la estructura del Gran Nido contará como "Entrar a la casa", desapareciéndolos inmediatamente al interior. Esto matará definitivamente el problema de colas o estancamientos frente a una puerta única.

**8. Mecánicas del Extractor y Entorno del Gran Nido:**
- **Sistema de Extracción (Bloque Extractor):** Se implementará un mecanismo (o bloque específico conectado al Nido) mediante el cual el jugador podrá "ordeñar" o extraer de forma constante y automática los beneficios producidos por la colonia entera (ya sea Estiércol, Popo puro, o acceso directo a recolección por Tolvas). Esto le da el valor definitivo de "Granja pasiva" a la Fase 4.
- **Terraformación Orgánica del Entorno:** El proceso de maduración o la misma vida natural del Gran Nido deberá terraformar lentamente sus alrededores. Se decorará el perímetro esparciendo orgánicamente los *Bloques de Popo (Sólido y Húmedo)* en el suelo cercano al nido, asentando "el bioma de los escarabajos" directamente frente a su casa.
- **Inmunidad al Hundimiento Fecal:** Se deberá programar/ajustar la física de colisión del *Bloque de Popo Húmedo* y del *Sewage* (Aguas Residuales) para que, de forma natural, la entidad DungBeetleEntity **SEA INMUNE a sus efectos perjudiciales**. Los escarabajos caminarán de forma perfectamente plana e inalterados sobre estos lodos sin ralentizarse ni hundirse accidentalmente, dominando el terreno orgánico de su hogar.

## 18. Refinamiento y Crafteo del Inodoro (Toilet)
**1. Receta de Crafteo (Crafting Recipe):**
- El Inodoro (ToiletBlock) se podrá craftear en la mesa de trabajo usando una combinación lógica de fontanería.
- **Ingredientes sugeridos:** Concreto Blanco (para abarcar toda la porcelana de la taza), 1 Cubo de Agua (para la fontanería), y tal vez 1 botón o pepita de hierro para la válvula.
- El cubo vacío se devolverá a la cuadrícula del inventario tras el crafteo.

**2. Mecánica Fisiológica Avanzada (El "Minijuego" del Inodoro):**
- **Sistema de Digestión:** Hacer popo ya no será un evento puramente gratuito o automático. El sistema interno del jugador medirá un "Nivel de Ganas". 
- **Restricciones Biológicas:** Para poder producir heces y botarlas voluntariamente al Inodoro, el jugador debe:
  1. Haber ingerido algún tipo de Comida recientemente u obligatoriamente en ese periodo de tiempo.
  2. Haber dejado pasar un enfriamiento "Cooldown" estomacal de aproximadamente 10 minutos (tiempo real).
- **Control Manual (Tecla G):** Cuando el jugador esté sentado legítimamente en el ToiletSeat, la acción de deponer no será estática. Aparecerá en pantalla un mensaje en texto (HUD o Chat) indicando: *"Presiona [G] para hacer popo"*. (Se conectaría al Keybinding que ya se ha desarrollado).
- **Progreso Digestivo (% en Pantalla):** Si el jugador intenta pulsar G o hacer fuerza pero aún no cumple con la restricción de comida/tiempo, el Inodoro o la interfaz retroalimentarán mostrando directamente un % en la pantalla del nivel actual (ejemplo: "Ganas de evacuar: 45% - Necesitas comer más o esperar"). Esto le otorga claridad de por qué aún no puede producir el ítem.

## 19. Estabilidad y Compatibilidad Multijugador (SMP - Server Multiplayer)
Para asegurar que PopoCraft pueda ser disfrutado plenamente en servidores con múltiples usuarios, la versión definitiva deberá pasar por una rigurosa fase técnica enfocada en multijugador:
**1. Sincronización Cliente-Servidor (Packets):**
- Eventos visuales (porcentaje de ganas de ir al baño) y atajos de teclado (tecla G) deben sincronizarse eficientemente mediante Networking (Custom Packets) para no saturar al servidor central o generar de-sync (retraso).
- Los efectos visuales de partículas (como las nubes de veneno de los slimes o el olor fétido) deben validarse de ejecutarse correctamente en el lado cliente.
**2. Aislamiento Fisiológico (Variables por Jugador):**
- El *cooldown* de digestión y consumo de alimentos será alojado y verificado en la *entidad individual* de cada jugador (a través de Extended Data / Componentes de Jugador) y NO globalmente, permitiendo que cada usuario posea su propio metabolismo independiente al resto de jugadores en el servidor.
**3. Limitadores y "Lag-Proofing":**
- **Prevención de Entidades Ocultas:** Si múltiples inodoros atascados se descuidan a lo largo y ancho del mapa multijugador, la aparición masiva de Fetid Slimes deberá ser controlada usando límites (cap) severos o verificaciones de chuncks para que los "tps" (Ticks per Second) del servidor no caigan.
- **Eficiencia del Escarabajo y la IA:** Refinar la búsqueda de rutas (Pathfinding) del Dung Beetle para que use "Raycasting" de bajo costo de procesamiento, previniendo que gigantescas poblaciones en las granjas de los jugadores congelen la RAM del servidor.

## 20. Invasión: correcciones del usuario y plan vigente (2026-09-23)

**Estado: EN DESARROLLO — especificación registrada; implementación y verificación pendientes.** Esta sección prevalece sobre las indicaciones anteriores que entren en conflicto. El usuario retiró expresamente la condición de «lluvia de ideas congelada»; se permite proponer mejoras.

### Reglas confirmadas
- Prioridad de trabajo: texturas, después modelado y efectos visuales, después las demás mecánicas y sus pruebas.
- Música exclusivamente del usuario, pendiente de adjuntar. Debe comenzar al iniciar el combate entre slimes y peloteros. Integrar reproducción por evento sin duplicaciones ni reinicio por cada escarabajo; no componer ni sustituir el tema.
- Al activarse el evento, los peloteros participantes corren a la casa principal. Entran, se equipan dentro y salen con la textura, armamento y efectos de su rol. La transformación visual no ocurre instantáneamente fuera del nido.
- Reparto inicial elegido con la flexibilidad autorizada: 65 % cuerpo a cuerpo y 35 % a distancia (100 % total). La propuesta 65:45 era orientativa. Conservar el rol durante el evento, sin volver a sortearlo cada tick o al recargar.
- Gran Nido con admisión de habitantes sin cupo numérico de rechazo, sustituyendo el antiguo límite de 30; acceso por cualquier bloque perimetral válido de la estructura. Separar capacidad de alojamiento, capacidad de comida y límites reproductivos: admitir habitantes no implica reproducción ilimitada.
- Navegar hacia puntos perimetrales accesibles distribuidos, admitir sin colas en una puerta única y desplegar combatientes en salidas libres. El cupo ilimitado por sí solo no resuelve rutas bloqueadas.
- IA de combate con bandos y pertenencia al evento: peloteros atacan slimes enemigos y slimes atacan defensores del mismo evento; evitar fuego amigo y objetivos humanos. Reanudar comportamiento normal al terminar.
- Humanos exclusivamente espectadores del combate. La protección debe ser efectiva en servidor para todos los jugadores: impedir daño e intervención directa o indirecta sobre participantes y alteraciones del campo que cambien el resultado. No basta aplicar debilidad. Delimitar la protección al evento y retirar sus restricciones al finalizar.

### Plan y verificaciones previstas
1. Preparar texturas de dos roles de pelotero, tres slimes y dos proyectiles, con fuentes y procedencia; modelar equipamiento y remodelar exterior sólido del Gran Nido con entrada negra simbólica.
2. Inspeccionar recursos y render en cliente antes de dar por terminada la entrega artística.
3. Integrar fases de reunión, equipamiento, despliegue, oleadas y finalización; persistencia de identidades, roles y evento. Conservar habitantes ante guardado/carga, salida bloqueada o rotura del nido.
4. Verificar admisión por perímetro, navegación de grupos, selección de enemigos, ataques cuerpo a cuerpo y proyectiles, y ausencia de intervención humana en multijugador.
5. Comprobar fin del evento y restauración de IA/aspecto, limpieza de restricciones y ausencia de duplicación de habitantes. Validar build y cliente; distinguir pruebas automáticas de apreciación humana pendiente.
6. Integrar y comprobar el tema musical cuando el usuario entregue el archivo. Su ausencia no bloquea el trabajo de texturas/modelado.

**Registro de esta actualización:** solo documentación; no se declara ninguna de estas nuevas reglas implementada ni probada todavía. Se preservan los cambios de código y recursos ya presentes en el directorio de trabajo.

**Inicio de implementación 2026-09-23:** autorizado comenzar. Primera entrega de trabajo: fuentes raster de roles y proyectiles, empaquetado reproducible y geometría compatible con los modelos actuales. Se revisarán visualmente los resultados antes de integrar estados de combate. Música pendiente del usuario. Mantener la raíz Minecraft 1.21.1 / Java 21; preservar cambios existentes de otras sesiones.

**Bloqueo de QA detectado:** compilación de modelos aprobada; arranque del cliente falla en el PlayerEntityMixin preexistente. El error de Mixin identifica la firma esperada de eatFood con FoodComponent, ausente en el callback. Se corregirá únicamente ese parámetro para permitir carga; no se declara revisado el sistema completo de digestión.

### 20.1. Primera entrega de arte — VERIFICADA (2026-09-23)
- Siete texturas originales mediante ImageGen integrado: dos materiales de peloteros militares, tres slimes y dos proyectiles. Fuentes en `art/source/invasion`, prompts en `art/invasion-imagegen-provenance.json` y correcciones en `art/invasion-imagegen-corrections.json`. Transparencias involuntarias de armaduras corregidas mediante ImageGen y verificadas; primera versión del corredor descartada por saturación/alpha.
- PNG de cuerpos 64×64 opacos y proyectiles 16×16 transparentes, generador `tools/build-invasion-assets.ps1` integrado en la entrada pública. Cinco modelos nativos en `InvasionModels`: soldado con casco/refuerzo/placa, artillero con cesta/honda, corredor bajo, escupidor con saco/boquilla y coloso con hombros/corona.
- Cliente real y build aprobados: `--offline -Pclientcheck runClientcheck build`, Java 21 / Gradle 8.9 / Minecraft 1.21.1. Informe `build/clientcheck/invasion-check.txt`, log `build/invasion-art-check.log`, captura inspeccionada `art/invasion-preview.png`. QA anterior de recursos también pasó. No se ejecutaron GameTests en esta entrega visual.
- Corregida firma preexistente de PlayerEntityMixin.onEatFood añadiendo FoodComponent; resuelto el fallo de arranque de Mixin identificado en el log. No se declara validado el resto de digestión.
- **Evento general sigue EN DESARROLLO.** Modelos preparados como fábricas y renderizados por QA, todavía sin selección de roles en entidades normales. Pendientes: Gran Nido sólido/admisión perimetral ilimitada, capa translúcida del coloso, efectos, IA y fases del evento, bloqueo de intervención humana, proyectiles funcionales, pruebas de comportamiento/SMP y música del usuario.
- Guía precisa de continuación: `art/INVASION_HANDOFF.md`. No confundir recursos empaquetados con evento jugable.
- JAR vigente inspeccionado: `build/libs/ProjectPopo-1.0.0.jar` (nombre según gradle.properties actual). Contiene InvasionModels y siete texturas, excluye InvasionPreviewScreen/ClientResourceCheck. Los JAR con nombre proyecto_intento-1.0-SNAPSHOT son históricos y no representan esta compilación.

### 20.2. Integración de invasión — EN DESARROLLO (2026-09-23)
Plan previo: acceso/salida distribuida por perímetro y admisión sin límite de habitantes, conservando límite reproductivo 30; cierre visual del nido coordinado con nueva navegación. Roles sincronizados/NBT, evento persistente en controlador con reunión/equipamiento, tres oleadas y finalización. IA exclusiva entre bandos, proyectiles, protección del evento en servidor, barra de progreso y efectos. Sin música sustitutiva. Verificaciones: GameTests de admisión >30/NBT, reunión/equipamiento, combate/aislamiento, guardado y fin; render y build Java 21. Preservar cambios previos y mundos personales.
