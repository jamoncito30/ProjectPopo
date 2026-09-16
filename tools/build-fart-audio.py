"""Original procedural SFX; no samples extracted from videos or third-party recordings.

Reproduce: python tools/build-fart-audio.py
Requires numpy and soundfile (local build/audio-deps is supported).
"""
from pathlib import Path
import sys
import json

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / 'build/audio-deps'))
import numpy as np
import soundfile as sf

destination = ROOT / 'src/main/resources/assets/proyecto_intento/sounds'
destination.mkdir(parents=True, exist_ok=True)
report = []
for index, duration in enumerate((0.48, 0.64, 0.79), 1):
    rng = np.random.default_rng(20260914 + index)
    rate = 44100
    t = np.arange(int(rate * duration)) / rate
    progress = t / duration
    # Uneven low lip-like vibration, gradually dropping in pitch, plus fluttering air.
    drift = np.interp(t, np.linspace(0, duration, 22), rng.uniform(-17, 17, 22))
    frequency = 108 - 55 * progress + drift + 9 * np.sin(2*np.pi*27*t)
    phase = np.cumsum(frequency) * (2*np.pi/rate)
    vibration = sum(np.sin(h*phase + 0.2*h) / h**0.85 for h in range(1, 15))
    flutter = 0.55 + 0.45*np.sin(2*np.pi*(19*t + 6*t*t))**2
    noise = rng.normal(0, 1, len(t))
    air = np.convolve(noise, np.ones(11)/11, mode='same')
    envelope = np.minimum(1, t/.012) * np.minimum(1, (duration-t)/.085)
    envelope *= (1-progress)**0.45
    signal = np.tanh(1.5*vibration) * flutter * 0.65 + air * (0.35 + progress*.25)
    signal = np.convolve(signal, np.ones(5)/5, mode='same') * envelope
    signal -= signal.mean()
    signal *= .72 / np.max(np.abs(signal))
    path = destination / f'fart_{index}.ogg'
    sf.write(path, signal, rate, format='OGG', subtype='VORBIS')
    decoded, actual_rate = sf.read(path)
    assert actual_rate == rate and decoded.ndim == 1 and np.max(np.abs(decoded)) < .99
    report.append({'file': path.name, 'seconds': len(decoded)/rate, 'sample_rate': actual_rate,
                   'channels': 1, 'peak': float(np.max(np.abs(decoded))), 'bytes': path.stat().st_size})
(ROOT/'art/fart-audio-provenance.json').write_text(json.dumps({
    'source': 'Original procedural synthesis with deterministic seeds; no video audio copied.',
    'reference_requested': 'https://www.youtube.com/watch?v=cl0SVX78XM4',
    'reference_access': 'Web tool could not load the video; it was not listened to.',
    'generator': 'tools/build-fart-audio.py', 'files': report}, indent=2), encoding='utf-8')
print(json.dumps(report, indent=2))
