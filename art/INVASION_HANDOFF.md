# Invasión: primera entrega visual (2026-09-23)

Estado: siete texturas y cinco fábricas de modelos preparadas y verificadas en cliente. El evento sigue EN DESARROLLO; no hay invasión jugable ni selección de estos modelos en los renderizadores de entidades normales todavía.

## Recursos

- Fuentes: `art/source/invasion/`. Las variantes `_opaque` son las fuentes finales de los dos peloteros; se conservan sus primeras versiones.
- Prompts completos: `art/invasion-imagegen-provenance.json` y `art/invasion-imagegen-corrections.json`. Herramienta integrada ImageGen, sin CLI/API externa.
- Materiales finales: `textures/entity/invasion/{beetle_soldier,beetle_artillery,slime_runner,slime_artillery,slime_colossus}.png`, 64×64, completamente opacos. Son materiales de superficie para geometría, no retratos ni skins vanilla desplegadas.
- Proyectiles: `textures/item/invasion/{dung_shot,acid_spit}.png`, 16×16 con alpha. Todavía sin registro de ítem/entidad ni comportamiento balístico.
- Empaquetado: `tools/build-invasion-assets.ps1`, también llamado por `tools/build-assets.ps1`. Solo recorta margen transparente de iconos y remuestrea nearest-neighbor; no dibuja ni recolorea arte.
- Geometría: `InvasionModels.beetle(false/true)` y `InvasionModels.slime(0/1/2)`. Mantienen los nombres de piezas que esperan las clases actuales de modelo. La futura integración debe almacenar/reutilizar los modelos en el renderer, no construirlos cada frame como hace la pantalla aislada de QA.
- Soldado: casco, refuerzo frontal y placa dorsal. Artillero: cesta, brazos de honda y bolsa. Corredor: bajo y ancho; escupidor: saco elevado y boquilla; coloso: hombros y corona.

## Evidencia

- `--offline -Pclientcheck runClientcheck build`: BUILD SUCCESSFUL con Java 21 / Gradle 8.9 / Minecraft 1.21.1.
- `build/clientcheck/invasion-check.txt`: siete PNG decodificados, dimensiones, opacidad de cuerpos y cinco modelos comprobados. Las comprobaciones de recursos anteriores también se ejecutaron.
- Captura real inspeccionada: `art/invasion-preview.png`. La pantalla está en `src/clientcheck`, fuera del JAR distribuible.
- `build/invasion-art-check.log` contiene la ejecución final. No se ejecutaron GameTests de comportamiento en esta entrega artística.
- Se corrigió un bloqueo preexistente de arranque en `PlayerEntityMixin.onEatFood`: faltaba el parámetro `FoodComponent` que exige la firma 1.21.1. El cliente ahora aplica el mixin y arranca. Esto no certifica persistencia ni balance de la digestión.

## Continuación obligatoria

1. Remodelar Gran Nido sólido con entrada simbólica, coordinando geometría/colisión con admisión perimetral; no cerrar la puerta activa antes de sustituir su navegación.
2. Conectar roles sincronizados y persistentes a los renderizadores. Añadir efectos de equipamiento y combate; la translucidez del coloso requiere una capa exterior y núcleo interior, todavía pendientes.
3. Implementar evento y pruebas: reunión en casa, entrada y cambio de rol dentro, salida segura, 65/35, objetivos enemigos, proyectiles y finalización/restauración. La pantalla QA no demuestra esta lógica.
4. Sustituir límite de admisión 30 sin eliminar límites reproductivos ni truncar habitantes guardados. Preservar UUID, nombres y datos; cubrir guardado/carga y destrucción.
5. Bloquear intervención humana en servidor y verificar multijugador. Revisar también las mecánicas de secciones 18–19 ya presentes pero no certificadas.
6. Música pendiente exclusivamente del archivo del usuario; no generar sustituto.

La sección 20 de AI_MOD_DOCUMENTATION.md es la autoridad del diseño. No marcar el evento TERMINADO por el resultado de esta entrega visual.
