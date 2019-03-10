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

### Preparing the Pi

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

8.  Type `sudo raspi-config `into the terminal.

9.  Go to interfacing options \> and enable I2C.

10. Power down with `sudo powerdown` from the terminal by pressing and set the
    Pi aside. You will not be using it until sensor testing.

 

 
