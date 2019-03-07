import time
import sys
import busio
from board import *
import Adafruit_MPR121.MPR121 as MPR121
import Adafruit_LSM303.LSM303 as LSM303
import Adafruit_SSD1306 as SSD1306
import RPi.GPIO as GPIO
import Adafruit_GPIO.SPI as SPI
import smbus
from PIL import Image
from PIL import ImageDraw
from PIL import ImageFont

cap_touch = MPR121.MPR121()
accel_mag = LSM303()
disp = SSD1306.SSD1306_128_64(rst=24)

class Display:
    def __init__(self, width, height, padding):
        self._width = width
        self._height = height
        self._top = padding
        self._bottom = self._height - padding
        self._image = Image.new('1', (self._width, self._height))
        self._draw = ImageDraw.Draw(self._image)
        self._empty = self._draw.rectangle((0, 0, self._width, self._height), outline=0, fill=0)
        self._font = ImageFont.load_default()

        disp.begin()
        disp.clear()
        disp.display()

    def _get_draw(self):
        return self._draw

    def get_font(self):
        return self._font

    def display_off(self):
        disp.clear()

class StepDisplay(Display):
    def __init__(self, steps, width, height, padding):
        self._steps = steps
        Display.__init__(self, width, height, padding)

    def start_count(self):
        self._empty
        self._draw.text((0, self._top), "Steps", font=self._font, fill=255)
        self._draw.text((0, self._top + 8), str(self._steps))
        return True

    def update_count(self, count):
        self._draw.rectangle((0, 0, self._width, self._height), outline=0, fill=0)

        self._draw.text((0, self._top), "Steps", font=self._font, fill=255)
        self._draw.text((0, self._top + 8), str(count), font = self._font, fill=255)
        disp.image(self._image)
        disp.display()
    

class Accel:
    def __init__(self):
        self._accel = accel_mag.read()
        self._accel_x, self._accel_y= self._accel
        self._accel_z = 0
    
    def get_x(self):
        return self._accel_x
    
    def get_y(self):
        return self._accel_y
    
    def get_z(self):
        return self._accel_z

class TouchSensor:
    def __init__(self, touch, release):
        self._touch = touch
        self._release = release

        cap_touch.begin()
        cap_touch.set_thresholds(self._touch, self._release)

    def last_touch(self):
        return cap_touch.touched()

if (__name__ == "__main__"):
    accel = Accel()
    step_count = StepDisplay(0, disp.width, disp.height, -2)
    touch = TouchSensor(128, 64)
    num_steps = 0
    update_steps = False
    x = 0
    y = 0
    z = 0

    disp.begin()
    disp.clear()
    disp.display()


    while True:
        current_touched = touch.last_touch()
        if (update_steps == True):
            new_x = accel.get_x()
            new_y = accel.get_y()
            new_z = accel.get_z()

            if ((x != new_x) or (y != new_y) or (z != new_z)):
                x = new_x
                y = new_y
                z = new_z
                num_steps += 1
                step_count.update_count(num_steps)
                print('{0} steps'.format(num_steps))

        step_count.start_count()
        num_steps += 1
        step_count.update_count(num_steps)
        print('{0} steps'.format(num_steps))
        
        # for i in range(12):
        #     pin_bit = 1 << i

        #     if current_touched & pin_bit:
        #         print('{0} touched!'.format(i))
        #         if (i == 0):
        #             step_count.start_count()
        #         if (i == 1):
        #             if (update_steps == False):
        #                 update_steps = True
        #             else:
        #                 update_steps = False

        time.sleep(0.1)




            
