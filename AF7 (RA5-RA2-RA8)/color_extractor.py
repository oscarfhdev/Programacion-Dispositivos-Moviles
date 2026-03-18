from PIL import Image
from collections import Counter
import sys

try:
    img = Image.open('/home/oscarfh06/Documentos/OSCAR/2DAM/PROGRAMACION MOVIL/AF7 (RA5-RA2-RA8)/app/src/main/res/drawable/logo.png')
    img = img.convert('RGBA')
    pixels = img.getdata()
    
    # Ignorar pixels muy blancos, negros puros o transparentes
    valid_pixels = [p for p in pixels if p[3] > 200 and not (p[0]>240 and p[1]>240 and p[2]>240) and not (p[0]<15 and p[1]<15 and p[2]<15)]
    counter = Counter(valid_pixels)
    for p, count in counter.most_common(5):
        print(f"#{p[0]:02x}{p[1]:02x}{p[2]:02x} ({count} pixels)")
except Exception as e:
    print(str(e))
