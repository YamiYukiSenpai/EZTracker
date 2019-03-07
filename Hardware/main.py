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

class Display:
    def __init__(self, padding):
        self._disp = SSD1306.SSD1306_128_64(rst=24)
        self._width = self._disp.width
        self._height = self._disp.height
        self._top = padding
        self._bottom = self._height - padding
        self._image = Image.new('1', (self._width, self._height))
        self._draw = ImageDraw.Draw(self._image)
        self._empty = self._draw.rectangle((0, 0, self._width, self._height), outline=0, fill=0)
        self._font = ImageFont.load_default()

        self._disp.begin()
        self._disp.clear()
        self._disp.display()

    def _get_draw(self):
        return self._draw

    def get_font(self):
        return self._font

    def display_off(self):
        self._disp.clear()
        self._disp.display()
    
    def display_refresh(self):
        self._disp.begin()
        self._disp.clear()
        self._disp.display()

class StepDisplay(Display):
    def __init__(self, steps, padding):
        self._steps = steps
        Display.__init__(self, padding)

    def start_display_count(self):
        self._draw.rectangle((0, 0, self._width, self._height), outline=0, fill=0)

        self._draw.text((0, self._top), "Steps", font=self._font, fill=255)
        self._draw.text((0, self._top + 8), str(self._steps), font = self._font, fill=255)

        self._disp.image(self._image)
        self._disp.display()
        return True

    def update_count(self, count):
        self._steps = count
    

class Accel:
    def __init__(self):
        self._accel_mag = LSM303(hires=False)
        self._accel = self._accel_mag.read()
        self._accel_x, self._accel_y= self._accel
        self._accel_z = 0
    
    def get_x(self):
        return self._accel_x
    
    def get_y(self):
        return self._accel_y
    
    def get_z(self):
        return self._accel_z

    def get_data_accel(self):
        self._accel = self._accel_mag.read()
        self._accel_x, self._accel_y= self._accel

class TouchSensor:
    def __init__(self, touch, release):
        self._cap_touch = MPR121.MPR121()
        self._touch = touch
        self._release = release

        self._cap_touch.begin()
        self._cap_touch.set_thresholds(self._touch, self._release)

    def last_touch(self):
        return self._cap_touch.touched()

if (__name__ == "__main__"):
    accel = Accel()
    step_count = StepDisplay(0, -2)
    touch = TouchSensor(128, 64)
    num_steps = 0
    update_steps = False
    step_display = False
    x = 0
    y = 0
    z = 0

    test = False
    test2 = True

    step_count.display_refresh()

    while True:
        current_touched = touch.last_touch()
        if (update_steps == True):
            accel.get_data_accel()
            new_x = accel.get_x()
            new_y = accel.get_y()
            new_z = accel.get_z()

            if (step_display == True):
                step_count.start_display_count()
            else:
                step_count.display_off()

            if ((x != new_x) or (y != new_y) or (z != new_z)):
                x = new_x
                y = new_y
                z = new_z
                if (test2 == True):
                    print("X = {0}, Y = {1}".format(x, y))
                num_steps += 1
                step_count.update_count(num_steps)
                print('{0} steps'.format(num_steps))
        
        if (test == True):            
            step_count.start_display_count()
            num_steps += 1
            step_count.update_count(num_steps)
            print('{0} steps'.format(num_steps))
        
        for i in range(12):
            pin_bit = 1 << i

            if current_touched & pin_bit:
                print('{0} touched!'.format(i))
                if (i == 0):
                    if (step_display == False):
                        step_display = True
                    else:
                        step_display = False
                if (i == 1):
                    if (update_steps == False):
                        update_steps = True
                    else:
                        update_steps = False

        time.sleep(0.5)
    




            
