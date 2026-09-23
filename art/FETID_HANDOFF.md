# Slime fétido — Minecraft 1.21.1 / Java 21

Estado: integración implementada; verificación final de comportamiento en curso.

## Recursos

- Seis originales ImageGen en `art/source/*_imagegen.png`; prompts completos en `art/fetid-imagegen-provenance.json`.
- Textura de entidad 64×64 y objetos/materiales 16×16. Modelo propio con cuerpo, dos burbujas, goteo y cara, animación de deformación al saltar.
- Geometría editable: `art/fetid-slime-model.json`, `FetidSlimeModel.java` y `tools/build-fetid-models.py`.
- Regeneración: `powershell -NoProfile -ExecutionPolicy Bypass -File tools/build-fetid-assets.ps1`. Integrado también en `tools/build-assets.ps1`.
- Captura real del cliente: `art/fetid-slime-preview.png`. Informe de recursos: `build/clientcheck/fetid-check.txt`.

## Mecánicas

- `fetid_slime`: hostil, tamaño mediano fijo, 16 puntos de vida, daño base 3. Salta y ataca con IA de slime. No se divide. Botín de 1–3 `viscous_biomass`.
- Al morir crea una nube de radio inicial 2.5, duración 200 ticks, veneno I y náusea; salpicadura visual y sonido vanilla. No rompe bloques. Respeta la regla de botín de entidades.
- Inodoro: acumula uso ocupado; ocho popós cada 18000 ticks, 25% de atasco por producción. Un atasco de 72000 ticks cargados permite un intento de spawn cada 100 ticks; máximo dos slimes próximos, suelo y volumen libres, sin fluidos. No aparece en pacífico ni con `doMobSpawning=false`. Limpiar reinicia el contador. Tiempo en ticks, no tiempo con servidor apagado.
- `pestilence`: poción rara + biomasa en soporte de pociones; náusea 20 segundos. Pólvora/aliento convierten a arrojadiza/persistente con comportamiento vanilla. La bebible usa el nuevo icono; las otras variantes conservan las siluetas vanilla.
- `toxic_plunger`: desatascador + biomasa; lentitud IV durante 60 ticks, 250 usos. También desatasca.
- `sewage_bucket`: cubo vacío + biomasa + popó. Coloca aguas residuales viscosas **contenidas, sin propagación**. Frenan, impiden salto y aplican veneno I. Recoger con cubo vacío devuelve el contenido. No es un fluido del registro vanilla y no se bombea con APIs de fluidos.
- `pestilent_torch`: antorcha + biomasa. Luz 12, radio esférico 10 para bloquear movimiento entrante de entidades `Monster`; las que ya están dentro pueden salir y reciben repulsión. No bloquea teletransporte, proyectiles ni jugadores. El índice de antorchas cargadas se retira al romper/descargar.

## Prueba manual

```
/summon proyecto_intento:fetid_slime ~2 ~ ~
/give @s proyecto_intento:viscous_biomass 16
/give @s proyecto_intento:toxic_plunger
/give @s proyecto_intento:sewage_bucket
/give @s proyecto_intento:pestilent_torch
/setblock ~3 ~ ~ proyecto_intento:inodoro[clogged=true]
```

Comprobar animación, ataque, muerte sin división, nube y botín; preparar poción con biomasa, colocar/recoger aguas residuales y probar la antorcha con un husk. El aspecto y equilibrio en una partida normal quedan a revisión humana.

Esta entrega corresponde a la raíz 1.21.1. No se portó a `ports/minecraft-current` ni a `ports/test_262`.
