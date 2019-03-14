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
import struct
from PIL import Image
from PIL import ImageDraw
from PIL import ImageFont
import datetime
import pyrebase
import json

class EZdatabase:
    def __init__(self, email, password):
        self._email = email
        self._password = password

        config = {
            "apiKey": "AIzaSyDJ_VX1PCVjNkVJJK27xwsykxeKMaP7esU",
            "authDomain": "eztracker1-da005.firebaseio.com/",
            "databaseURL": "https://eztracker1-da005.firebaseio.com/",
            "projectId": "eztracker1-da005",
            "storageBucket": "eztracker1-da005.appspot.com"
        }

        firebase = pyrebase.initialize_app(config)
        auth = self._firebase.auth()
        user = auth.sign_in_with_email_and_password(self._email, self._password)
        self._ez_db = self._firebase.database()
        user_data = auth.get_account_info(self._user['idToken'])
        self._uid = user_data['users'][0]['localId']

    def get_steps(self, day):
        num_steps = self._ez_db(self._uid).child("steps").child("today").get()
        return num_steps.val()

    def update_steps(self, day, steps):
        self._ez_db.child(self._uid).child("steps").update({day: steps})

class Display:
    def __init__(self, padding):
        self._disp = SSD1306.SSD1306_128_64(rst=24)
        self._width = self._disp.width
        self._height = self._disp.height
        self._top = padding
        self._bottom = self._height - padding
        self._image = Image.new('1', (self._width, self._height))
        self._draw = ImageDraw.Draw(self._image)
        self._empty = self._draw.rectangle((0, 0, self._width, self._height),
                                           outline=0, fill=0)
        self._font = ImageFont.load_default()

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
        self._draw.rectangle((0, 0, self._width, self._height),
                             outline=0, fill=0)

        self._draw.text((0, self._top), "Steps", font=self._font, fill=255)
        self._draw.text((0, self._top + 8), str(self._steps),
                        font=self._font, fill=255)

        self._disp.image(self._image)
        self._disp.display()
        return True

    def update_count(self, count):
        self._steps = count


class Accel:
    def __init__(self):
        self._accel_mag = LSM303(hires=False)
        self._accel = self._accel_mag.read()
        self._accel_x, self._accel_y = self._accel
        # self._accel_z = 0

    def get_x(self):
        return self._accel_x

    def get_y(self):
        return self._accel_y

    def get_z(self):
        return self._accel_z

    def get_data_accel(self):
        self._accel = self._accel_mag.read()
        self._accel_x, self._accel_y = self._accel


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
    accel_sensitivity = 1
    x = 0
    y = 0
    z = 0
    new_x = 0
    new_y = 0

    test = False
    test2 = True

    step_count.display_refresh()

    while True:
        current_touched = touch.last_touch()

        print(str(y))
        print(str(x))

        if (step_display is True):
                step_count.start_display_count()
        else:
            step_count.display_off()

        if (update_steps is True):
            accel.get_data_accel()
            new_x = accel.get_x()[0]
            new_y = accel.get_y()
            #new_z = accel.get_z()

            #new_x = int(new_x[0])
            new_y = int(new_y[0])
            #new_z = int(new_z[0])

            if ((x != new_x) or (y != new_y)):
            # if (((x > (new_x + accel_sensitivity)) and
            #      (x < (new_x - accel_sensitivity))) or
            #     ((y > (new_y + accel_sensitivity)) and
            #      (y < (new_y - accel_sensitivity)))):
                                
                x = new_x
                y = new_y

                print(str(new_x + accel_sensitivity))
                print(str(new_y + accel_sensitivity))
                # z = int(new_z[0])
                if (test2 is True):
                    print("X = {0}, Y = {1}".format(x, y))
                num_steps += 1
                step_count.update_count(num_steps)
                print('{0} steps'.format(num_steps))

        if (test is True):
            step_count.start_display_count()
            num_steps += 1
            step_count.update_count(num_steps)
            print('{0} steps'.format(num_steps))

        for i in range(12):
            pin_bit = 1 << i

            if current_touched & pin_bit:
                print('{0} touched!'.format(i))
                if (i == 0):
                    if (step_display is False):
                        step_display = True
                    else:
                        step_display = False
                if (i == 1):
                    if (update_steps is False):
                        update_steps = True
                    else:
                        update_steps = False

        time.sleep(0.5)
