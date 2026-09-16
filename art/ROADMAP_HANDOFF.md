# Integración de la sección 11

## Inodoro y desatascador

- IDs existentes conservados: `inodoro`, `desatascador`, `estiercol`, `extractor_estiercol`.
- `ToiletBlock`: `facing=north/east/south/west`, `clogged=false/true`. Modelo de taza abierta, pedestal y cisterna; aspecto del agua cambia cuando se atasca. Ocupa un bloque. Clic derecho sienta; agacharse desmonta. Solo un jugador, sin pasajeros previos, con dos bloques de espacio superior. Romperlo desmonta al ocupante.
- `ToiletSeatEntity` es un vehículo invisible técnico, sincronizado por el sistema vanilla de pasajeros. No se guarda ni se puede invocar mediante /summon. Se descarta al quedarse vacío o perder el inodoro. No añade entidades de asiento permanentes.
- La producción cada 15 minutos y la causa/probabilidad del atasco **siguen asignadas a Antigravity**. Conectar esa lógica al asiento y al estado `ToiletBlock.CLOGGED`; no hacer un segundo asiento ni registrar otro inodoro. Definir si el temporizador mide sesión sentada o cooldown del jugador, y persistirlo donde corresponda para evitar reinicios por romper el bloque.
- `PlungerItem`: herramienta de 128 usos, atributos de espada de madera (daño base adicional 2, velocidad -2.4), empuje adicional 0.9 sujeto a resistencia vanilla. Desatascar consume un uso, nunca al estar limpio; sin desgaste en creativo. Contrato `PlungerItem.unclog(world,pos,player,hand)`.
- Recetas: inodoro con cinco cuarzos y un cubo (`Q Q / QBQ /  Q `); desatascador con dos palos y una bola de slime verticales. Ambos y fertilizante están en PopoCraft; no añadir el extractor a creativo.

## Fertilización

- `FertilizerItem.useOnBlock`: usar sobre cultivo o tierra cultivada. Una unidad trata hasta nueve suelos en 3×3 que tengan cultivos `CropBlock` inmaduros. No consume si no hay objetivos o ya están fertilizados; respeta permisos de modificación de toda el área.
- No aplica crecimiento al hacer clic. `FertilizedFarmlandBlock` avanza una edad cada 200 ticks (10 s a 20 TPS), durante seis intervalos. Necesita luz al menos 9. Al terminar vuelve a tierra cultivada vanilla conservando humedad y planta.
- Tiempo almacenado por `charges` en estado de bloque y ticks programados persistentes del chunk. Sin escaneos globales ni progreso con el chunk descargado; random ticks/humedad/pisoteo siguen siendo de FarmlandBlock.
- `CropBlockMixin` permite que los cultivos sobrevivan sobre la variante de suelo. El suelo usa modelos vanilla y no tiene ítem colocable; al romper devuelve tierra. Alcance actual: trigo, zanahorias, patatas y remolachas y otros CropBlock compatibles; no árboles, tallos de melón/calabaza ni nether wart.
- Producción/receta del estiércol desde el extractor: **pendiente de Antigravity**. Ya existe el ítem funcional para entregarlo desde inventario/generación futura. No crear otro ID llamado `estiervol`.

## Extractor y comerciante

- Extractor: textura 16×16 de rejilla marrón/cobre, modelo cúbico con parte superior de tierra seca. Registrado solo como bloque; se retiró el BlockItem provisional para cumplir que los jugadores no lo colocan. Aún no tiene entidad de bloque ni extracción propia: Antigravity implementa generación 1–3, conversión de material e inventario. El controlador comunitario existente sigue extrayendo popó por tolva, hasta que se conecte esa nueva etapa.
- Comerciante: textura `textures/entity/dung_beetle_trader.png` 64×64 y mochila en el modelo original. `DungBeetleEntity.isTrader()/setTrader(boolean)` usa DataTracker y NBT `IsTrader`. Puede comprobarse con `/summon proyecto_intento:dung_beetle ~ ~ ~ {IsTrader:1b}`. No ofrece comercio todavía: conectar el sistema Merchant y probabilidad del 5% asignados a Antigravity a este mismo indicador.
- `HasDroppedShell` ahora conserva su valor NBT en vez de escribirse siempre false. La caída automática sigue pendiente; se añadió modelo nativo para el ítem de caparazón ya registrado, evitando textura/modelo faltante.

## Generación y validación

- Entrada pública: `tools/build-assets.ps1`; nuevos `build-roadmap-assets.ps1` y `build-roadmap-models.py`.
- Fuentes y prompts: `art/roadmap-imagegen-provenance.json`, PNG en `art/source/*_imagegen.png`. ImageGen integrado; empaquetado nearest-neighbor, geometría JSON nativa.
- GameTests: `RoadmapGameTests` y casos ampliados de `GrandNestGameTests`. Cliente: `ClientResourceCheck` y `RoadmapPreviewScreen`; captura en `build/clientcheck/screenshots/expansion.png`.
- Java 21 / Gradle 8.9: `--offline -Pgametest -Pclientcheck runGametest runClientcheck build`.

## Revisión del refugio comunitario

Se corrigieron conservación al retirar reservas/desmontar capas, depósitos rechazados por almacén lleno, evacuación y devolución de materiales al romper el controlador, protección del volumen frente a bloques ajenos, validación de terreno/separación, ejecución de fundación solo en servidor y cada 200 ticks, consumo de comida sin pérdida parcial, traslado de residentes solo al confirmar spawn, copia inmutable del origen y persistencia acotada. El sexto material de una casa fundada se conserva como alimento. Audio de inicio se envía una vez a cada jugador, sin retransmitir a todos por cada destinatario. Se restauraron tres OGG faltantes y se reparó el icono inválido del avance de inicio.

Límites pendientes de la integración comunitaria: no hay un gestor persistente de propiedad para cada pieza ni reacción completa a rotura de piezas periféricas; `stage=4` del controlador sigue siendo la autoridad de completitud. La reproducción automática pequeña tiene una intermitencia histórica documentada; no se declara resuelta por estas correcciones. Antes de afirmar que toda la colonia funciona en todos los escenarios, añadir pruebas de ciclo autónomo completo desde cinco hogares, descargas de chunks durante migración y reparación/abandono de estructura dañada. No reintroducir sobrescrituras de bloques ni borrar carga cuando `deposit` devuelve cero.
