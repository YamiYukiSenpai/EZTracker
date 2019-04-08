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
import sys
import os
import subprocess
from requests.exceptions import HTTPError

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
        auth = firebase.auth()
        user = auth.sign_in_with_email_and_password(self._email, self._password)
        self._ez_db = firebase.database()
        user_data = auth.get_account_info(user['idToken'])
        self._uid = user_data['users'][0]['localId']
        self._ez_db.child(self._uid).child("steps").update({"realSteps": 0})

    # def get_steps(self, day):
    #     num_steps = self._ez_db(self._uid).child("steps").child(today).get()
    #     return num_steps.val()

    def update_steps(self, steps):
        self._ez_db.child(self._uid).child("steps").update({"realSteps": steps})

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
        self._font_heading = ImageFont.truetype(font="/usr/share/fonts/ubuntu/Ubuntu-B.ttf", size=20)
        self._font_body = ImageFont.truetype(font="/usr/share/fonts/ubuntu/UbuntuMono-R.ttf", size=50)
        self._font_status = ImageFont.truetype(font="/usr/share/fonts/ubuntu/UbuntuMono-R.ttf", size=15)
        self._draw.rectangle((0, 0, self._width, self._height),
                             outline=0, fill=0)
        self._disp.begin()

    def _get_draw(self):
        return self._draw

    def get_font(self):
        return self._font

    def display_off(self):
        self._disp.clear()
        self._disp.display()

    def refresh(self):
        #self._disp.begin()
        self._disp.clear()
        self._disp.display()

    def reset(self):
        self._disp.reset()
    
    def display(self):
        self._disp.display()

    def get_height(self):
        return self._height

    def display_message(self, message, index, size=None, body=1):
        # self._draw.rectangle((0, 0, self._width, self._height),
        #                      outline=0, fill=0)

        if (size is not None):
            self._font_body = ImageFont.truetype(font="/usr/share/fonts/ubuntu/UbuntuMono-R.ttf", size=size)
        if (body == 0):
            self._draw.text((0, self._top + index), message, font=self._font_body, fill=255)
        else:
            self._draw.text((0, self._top + index), message, font=self._font_heading, fill=255)
        self._disp.image(self._image)
        #self._disp.display()
        return True


class StepDisplay(Display):
    def __init__(self, steps, padding):
        self._steps = steps
        self._status = "Stopped"
        Display.__init__(self, padding)

    def start_display_count(self, stop=0):
        self._draw.rectangle((0, 0, self._width, self._height),
                             outline=0, fill=0)

        self._draw.text((0, self._top), "STEPS", font=self._font_heading, fill=255)
        self._draw.text((0, self._top + 10), str(self._steps),
                        font=self._font_body, fill=255)
        self._draw.text((0, self._bottom - 18), str(self._status),
                        font=self._font_status, fill=255)
        self._disp.image(self._image)
        self._disp.display()
        return True

    def update_count(self, count):
        self._steps = count
    
    def status(self, curr_status=None):
        if (curr_status is True):
            self._status = "Running..."
        else:
            self._status = "Stopped"


class Accel:
    def __init__(self):
        self._accel_mag = LSM303(hires=False)
        self._accel, self._mag = self._accel_mag.read()
        self._accel_x, self._accel_y, self._accel_z = self._accel

    def get_x(self):
        return self._accel_x

    def get_y(self):
        return self._accel_y

    def get_z(self):
        return self._accel_z

    def get_data_accel(self):
        self._accel, self._mag = self._accel_mag.read()
        self._accel_x, self._accel_y, self._z = self._accel
        return (self._accel, self._mag)


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
    # u_email = "konakonata@outlook.com"
    # u_pass = "password"
    u_email = str(sys.argv[1])
    u_pass = str(sys.argv[2])
    disp = Display(-2)
    auth_disp = Display(-2)
    try:
        lock = open(".~ezlock", 'r')
    except OSError:
        lock = open(".~ezlock", 'w')
        lock.write("Please don't edit this or whatnot")
        lock.close()
    else:
        login_status = open("error.txt", "w")
        login_status.write("2")
        login_status.close()
        sys.exit()
    try:
        auth_disp.display_message("Validating...", index=0, size=20)
        auth_disp.display()
        ez_db = EZdatabase(u_email, u_pass)
    except HTTPError:
        auth_disp.reset()
        auth_disp.refresh()
        auth_disp.display_message("Wrong", index=20, size=20)
        auth_disp.display_message("password", index=40, size=20)
        auth_disp.display()
        login_status = open("error.txt", "w")
        login_status.write("1")
        login_status.close()
        os.remove(".~ezlock")
        sys.exit()
    else:
        auth_disp.refresh()
        auth_disp.display_message("Success!", index=20, size=25)
        auth_disp.display()
        login_status = open("/var/www/html/error.txt", "w")
        login_status.write("0")
        login_status.close()
    disp.refresh()
    accel = Accel()
    step_count = StepDisplay(0, -2)
    touch = TouchSensor(255, 255)
    num_steps = 0
    update_steps = False
    step_display = False
    accel_sensitivity = 160
    network = False
    pi_direct_wlan = False
    x = 0
    y = 0
    z = 0
    new_x = 0
    new_y = 0
    new_z = 0

    test = False
    test2 = True
    test3 = True

    step_count.refresh()

    while True:
        current_touched = touch.last_touch()

        print('X = {0}, Y = {1}, Z = {2}, new_x = {3}, new_y = {4}, new_z = {5}'.format(x, y, z, new_x, new_y, new_z))

        if (step_display is True):
                step_count.start_display_count()
        else:
            step_count.display_off()

        if (update_steps is True):
            data_accel, data_mag = accel.get_data_accel()
            new_x, new_y, new_z = data_accel
            if (((x > (new_x + accel_sensitivity)) or
                 (x < (new_x - accel_sensitivity))) or
                ((y > (new_y + accel_sensitivity)) or
                 (y < (new_y - accel_sensitivity))) or
                ((z > (new_z + accel_sensitivity)) or
                 (z < (new_z - accel_sensitivity)))):
                                
                x = new_x
                y = new_y
                z = new_z

                if (test2 is True):
                    print("X = {0}, Y = {1}".format(x, y))
                num_steps += 1
                step_count.update_count(num_steps)
                if (test2 is True):
                    print('{0} steps'.format(num_steps))
                ez_db.update_steps(num_steps)

        for i in range(12):
            pin_bit = 1 << i

            if current_touched & pin_bit:
                if (test3 is True):
                    print('{0} touched!'.format(i))
                if (i == 0):
                    if (step_display is False):
                        step_display = True
                    else:
                        step_display = False
                if (i == 2):
                    if (update_steps is False):
                        update_steps = True
                    else:
                        update_steps = False
                    step_count.status(update_steps)
                if (i == 8):
                    # disp.reset()
                    # disp.clear()
                    disp.refresh()
                    disp.display_message("Program", index=0)
                    disp.display_message("closed", index=20)
                    disp.display()
                    os.remove(".~ezlock")
                    # os.remove("error.txt")
                    sys.exit()
                    # if (pi_direct_wlan is False):
                    #     disp("Activating WiFi direct", 0)
                    #     subprocess.call("sudo ifdown wlan0")
                    #     subprocess.call("sudo ifup wlan0")
                    #     wlan_dir = subprocess.call("wpa_cli -ip2p-dev-wlan0 p2p_group_add persistent=0")
                    #     if (wlan_dir == "OK"):
                    #         disp("Please connect to DIRECT-PB-RPi3", 2)
                    #     else:
                    #         disp("Error", 2)
                    # else:
                    #     disp("Deactivating WiFi direct", 0)
                    #     subprocess.call("")
        time.sleep(0.5)

