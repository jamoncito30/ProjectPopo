# Gran refugio: contrato visual de integración

## Recursos

- Generar: `python tools/build-grand-nest-assets.py` (Python estándar, sin paquetes).
- Entrada global: `tools/build-assets.ps1`.
- Plano empaquetado: `data/proyecto_intento/grand_nest/blueprint.json`.
- Modelos: `assets/proyecto_intento/models/block/grand_nest/stage_1..4/piece_0..124.json`.
- Colisión: `assets/proyecto_intento/grand_nest_shapes.json`, generado del mismo diseño.
- Bloque técnico registrado: `proyecto_intento:grand_beetle_nest_piece[stage=1,piece=0]`.
- No tiene ítem, receta ni aparición automática. El controlador central sí contiene inventario; habitantes y montaje automático siguen pendientes.

## Montaje

Origen: esquina inferior noroeste. Volumen reservado: X/Z 0..4, Y 0..4. Entrada al norte (-Z). No hay rotación en esta versión.
Para cada entrada de `stages[n].pieces`, colocar el bloque en `origen + pos`, con `stage=n+1` y `piece` indicado. Fórmula `piece = x + 5*z + 25*y`.
Las posiciones ausentes son aire interior; **no colocar los 125 estados indiscriminadamente**, pues un bloque de geometría vacía sigue siendo un bloque para navegación y construcción.
Al cambiar de etapa, actualizar las piezas existentes, añadir las nuevas y retirar únicamente las piezas anteriores que pertenezcan a esta obra. Nunca borrar bloques ajenos. El presupuesto no está codificado en el modelo.

Etapas: 1 plataforma y primeras hiladas; 2 muros abiertos y arco; 3 bóveda incompleta; 4 cubierta cerrada y respiradero. Alturas respectivas: 12, 32, 56 y 80 unidades de modelo (16 = un bloque).
Texturas existentes húmeda/seca. Cuboides recortados a 0..16 por pieza; colisión coincide con el modelo. Base central `(2,0,2)` reservada para futuro controlador/extracción; la tolva se colocaría debajo, fuera del volumen.

## Pendientes funcionales (orden recomendado)

1. Ampliar el controlador persistente existente con identidad de colonia, origen, material invertido y plazas 30. Almacén 360 ya implementado. Definir coste definitivo (ver sección 10 del informe IA).
2. Reserva de terreno plano, volumen libre, acceso norte y separación de 10 bloques entre bordes. Evitar que varios escarabajos funden obras duplicadas.
3. Cinco casas válidas activan obra; aportar material por transacciones de una unidad. Transferir reservas y desmontar casas por fases, conservando residentes hasta poder migrar.
4. Migración con UUID, edad y datos intactos, tanto de entidades visibles como alojadas. Sustituir/adaptar el límite local anterior de 16, incompatible con 30 residentes.
5. Conectar la IA al inventario existente mediante `deposit(ItemStack)`. Ya existe límite 360 y extracción inferior por tolva en etapa 4. No confundir capacidad de comida con capacidad de residentes.
6. Al agotarse plazas, fundación de casas individuales usando excedentes. Persistencia, rotura parcial, descargas de chunks, reconstrucción y `mobGriefing` necesitan reglas y pruebas.
7. Soundtrack: esperar archivo del usuario. Registrar evento, distancia y disparo único de inicio sin reiniciar música por cada aporte/carga del chunk.

## Verificación reproducible

Java 21 / Gradle 8.9: `--offline -Pclientcheck runClientcheck build`.
QA carga los 500 modelos, contrasta ocupación visual/colisión y renderiza las cuatro etapas. Captura `build/clientcheck/screenshots/gran-refugio.png`; informe `build/clientcheck/grand-nest-check.txt`.
La pantalla QA pertenece a `src/clientcheck`, excluida del JAR distribuible. No modifica mundos guardados. Para pruebas de mecánicas futuras, añadir GameTests de montaje, economía, traslado de UUID y automatización antes de declarar la colonia terminada.

## Controlador y almacén implementados (continuación 10.1)

- Bloque `proyecto_intento:grand_beetle_nest_controller[stage=1..4]`, sin ítem. Entidad de bloque `proyecto_intento:grand_beetle_nest`. Al montar el plano, **sustituir la pieza 12 de cada etapa por este controlador**, en `origen + (2,0,2)`. Tiene únicamente propiedad `stage`; no pasar `piece`. Su modelo/colisión coincide con la pieza central de suelo.
- Inventario `GrandBeetleNestBlockEntity`: seis huecos de 60, capacidad total 360; solo popó. Serialización vanilla `Items` mediante `Inventories`, conserva componentes de cada stack. No hay menú de jugador.
- API para la futura IA: `deposit(ItemStack offered)` en servidor y fase 4. Devuelve cantidad aceptada y **descuenta esa misma cantidad del stack recibido**; el sobrante permanece. No descontar nuevamente la carga del escarabajo. Con carga booleana actual, crear stack de una unidad y borrar la carga solo si devuelve 1. No usar `setStack` para transferir comida: es la operación estándar de reemplazo de un hueco, no un depósito transaccional.
- `getFoodCount()` suma el almacén; `isComplete()` consulta fase. Construcción y materiales invertidos aún no existen: esta API es para alimento/almacenamiento posterior, no para avanzar fases.
- Tolvas: acceso únicamente por cara inferior cuando `stage=4`; no admiten entrada por automatización. Tolva bajo `origen + (2,0,2)`. Fases 1..3 conservan el inventario pero bloquean extracción. Cambiar `stage` en el mismo bloque conserva su entidad/NBT.
- Romper/reemplazar controlador suelta todos los ítems y vacía el inventario desprendido. Romper una pieza periférica todavía NO invalida el controlador: el futuro gestor multibloque debe comprobar integridad y decidir qué hacer. Por ahora un controlador técnico aislado en etapa 4 funciona; no se genera en supervivencia ni se declara un edificio válido por sí solo.
- QA: `GrandNestGameTests` comprueba límite, rechazo, sobrantes, NBT, tolva real por etapa y devolución de 360 al romper. La suite completa pasó 29/29 en `build/grand-store-tests.log`.

Prueba técnica aislada: colocar controlador etapa 4 con `/setblock ~ ~ ~ proyecto_intento:grand_beetle_nest_controller[stage=4]`; colocar tolva debajo. Sembrar un hueco para QA con `/data merge block X Y Z {Items:[{Slot:0b,id:"proyecto_intento:popo",count:60}]}` usando las coordenadas reales del controlador. El comando es ayuda manual; la prueba automática verificada usa la API Java y una tolva vanilla real.
