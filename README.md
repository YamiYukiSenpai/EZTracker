![](https://i.imgur.com/JnPIQFU.png)

Presented by: JRD Developers
----------------------------

Simplfying the current pedometer for the aging generation looking to get
healthy. Most older users are turned off by complex devices and UIs. Being able
to monitor their own health by implementing a simple and user-friendly interface
can lead to a more active lifestyle.

 

Build Instructions
------------------

Firstly, a list of parts will be required in order to recreate this project.
Below is price a breakdown of the prices and parts for each component used to
get the project to completion. Spreadsheet with hyperlinks can be found
[here.](https://github.com/YamiYukiSenpai/EZTracker/blob/master/Documentation/EZ_Parts_Budget.xlsx)

![](https://github.com/YamiYukiSenpai/EZTracker/blob/master/Documentation/Pictures/build1.png)

Assembly
--------

### Step 1: Preparing the Pi

In this step we will cover basic Raspberry Pi imaging so you are able to login
and access your device to test and drive your sensors. Download the latest
[Raspberry Pi image](https://www.raspberrypi.org/downloads/). Our recommendation
is NOOBS. This will ensure that starting off, you will have almost everything
you need if you decide to re-purpose the device later on.

1.  Download [etcher](https://www.balena.io/etcher/). This program will allow
    you to burn the Raspberry Pi image to your SD card.

2.  Insert your SD card into the SD card reader and plug it into your computer.

3.  Open etcher and follow the on screen instructions to burn your image. We
    found this program the easiest to use. [Extra documentation if
    needed](https://www.raspberrypi.org/documentation/installation/installing-images/README.md).

4.  Insert the SD card into your Raspberry Pi along the underside of the device,
    logo facing out. Plug all of the required cables in such as: Ethernet, HDMI
    cable, mouse, keyboard, power, and turn the device on.

5.  Upon boot you will see an option for different operating systems. Select
    **Raspbian** and follow the on screen instructions to complete the OS
    installation.

6.  At this point the Pi should boot to desktop. Follow the additional set up
    options on screen.

7.  Enable I2C. This is the method we will be using to communicate with our
    Raspberry Pi. Type `sudo apt-get install -y python-smbus i2c-tools` into the
    terminal by pressing `ctrl+alt+t.`

8.  Type `sudo raspi-config` into the terminal.

9.  Go to interfacing options \> and enable I2C.

10. Power down with `sudo powerdown` from the terminal by pressing and set the
    Pi aside. You will not be using it until sensor testing.

 

### Step 2: Breadboarding and Prototyping

Here we will cover basic sensor connectivity to the Raspberry Pi using a
breadboard for creating mock layout/design that will be used in the PCB creation
stage.

1.  Gather the following items: Breadboard, LSM303 Sensor, MPR121, 128x64  
    LCD sensor, and 4 Female-to-Male GPIO cables.

2.  Identify the labels on the sensor. For basic usage, we will be using: 3.3v,
    GND, SDA, and SCL.

3.  Identify the corresponding pinouts on the Raspberry Pi. [This website is a
    great tool to use if you are unsure.](https://pinout.xyz/)

4.  Plug in the female part of the GPIO cables into the Raspberry Pi’s 1,3,5,
    and 6 pins, based on the chart from pinout.xyz. These are the pins we are
    going to be using for this project.

5.  Using the connectors that came with the sensors, plug them into the
    breadboard and rest the sensors accordingly. Below is an example.

![](https://github.com/YamiYukiSenpai/EZTracker/blob/master/Documentation/Pictures/build2.png)

6.  Connect the corresponding cables from the Raspberry Pi into the matched
    holes for the sensor.

7.  (Optional but recommended) Power on the Raspberry Pi to see if the sensors
    are being detected correctly by viewing their address values.

8.  (Optional but recommended) Open the terminal with *ctrl + alt + t* and run
    the command: *i2cdetect –y 1.* Ideally, the resulting output should be as
    follows, if not double check your connections and try again.

![](https://github.com/YamiYukiSenpai/EZTracker/blob/master/Documentation/Pictures/build3.png)

9.  Voila! You have your mock up sensor connection!
