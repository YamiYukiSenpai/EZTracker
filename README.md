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

8.  (Optional but recommended) Open the terminal with `ctrl + alt + t` and run
    the command:` i2cdetect –y 1`*.* Ideally, the resulting output should be as
    follows, if not double check your connections and try again.

    ![](https://github.com/YamiYukiSenpai/EZTracker/blob/master/Documentation/Pictures/build3.png)

9.  Voila! You have your mock up sensor connection!

PCB Design
----------

This is part of the project that needs to be proceeded with care and caution. It
is advised to double check your designs before purchasing etching and cutting
services. For this step, [Fritzing](http://fritzing.org/download/) will be used.
It is an open-source application that allows the user to easily create PCB
schematics for different development platforms. It is highly customizable and
easy to use.

### Step 1: Fritzing

1.  [Download](http://fritzing.org/download/) and extract Fritzing. Installation
    notes are on the linked page for various operating systems.

2.  (Optional) Download the [AdaFruit Fritzing
    Library.](https://github.com/adafruit/Fritzing-Library) Handy if you want to
    take the extra step and create a mock or your own connection/designs in
    Fritzing.

3.  Download our Fritzing file
    [here](https://github.com/YamiYukiSenpai/EZTracker/blob/master/pcb%20files/ez_v1.fzz)
    and open it. From the PCB tab, you can make changes at your leisure and pick
    it apart to see how it was made. Below is an image of our design.

    ![](https://github.com/YamiYukiSenpai/EZTracker/blob/master/Documentation/Pictures/build4.png)

4.  Export as a gerber file (the format primarily used in create a physical
    PCB). *File \> Export for Production \> Extended Gerber* and select an
    appropriate folder.

5.  Zip/Compress the folder containing the gerber files and send them to your
    etcher of choice.

### Step 2: Soldering

Once you have your PCB etched, we are ready to solder the parts together. Again,
please double check your design before finally soldering.

1.  Gather your sensors, copper wire, wire stripper, the pin headers that came
    with the sensor, your PCB, solder, and soldering iron.

    ![](https://github.com/YamiYukiSenpai/EZTracker/blob/master/Documentation/Pictures/build5.png)

2.  Solder your headers that came with your sensors, pictured above, to the
    corresponding holes. Put the longer end of the headers into your breadboard
    and place your sensor holes into the upright pins and solder all of the
    pins. This will ensure your sensor doesn’t move too much during soldering
    and a sturdy connection. **Note:** [Watch this video on soldering tips for
    additional help.](https://www.youtube.com/watch?v=oqV2xU1fee8) **Solder in a
    well ventilated area and use safety glasses.**

    ![](https://github.com/YamiYukiSenpai/EZTracker/blob/master/Documentation/Pictures/build6.png)

3.  Solder your vias on your PCB. The easiest method we found, was to strip your
    copper wiring, stick it into the breadboard, and slide one via onto it so
    that it is flat and stable for soldering. Imaged below is an example, and
    repeat for each via. Once each via is soldered, snip the excess wire with
    cutters.

    ![](https://github.com/YamiYukiSenpai/EZTracker/blob/master/Documentation/Pictures/build7.png)

4.  Solder your stackable headers. Using the breadboard again, take some extra
    copper wire from the snipped vias, or strip more, and place it into the
    female part of the header. The more, the sturdier. Flip the header over and
    plug it into the breadboard. Place your PCB onto the pins sticking out and
    solder the connections. Example placeholder image below.

    ![](https://github.com/YamiYukiSenpai/EZTracker/blob/master/Documentation/Pictures/build8.png)

5.  Repeat the process for the 2x20 header for the Raspberry Pi. It’s not
    entirely necessary to solder all of the holes, but it ensures the PCB does
    not bend as much.

6.  Your soldering should be complete. Plug everything in accordingly, and you
    should be ready for the next step! Example placeholder image below.

    ![](https://github.com/YamiYukiSenpai/EZTracker/blob/master/Documentation/Pictures/build9.png)

Power Up
--------

Once everything is plugged in, and you have double checked your connections,
power on your Raspberry Pi.

1.  Open up the terminal with `ctrl + alt + t` and run the command `i2cdetect –y
    1.`

2.  Hopefully everything is in working order. If so, you will see the following
    address values.

    ![](https://github.com/YamiYukiSenpai/EZTracker/blob/master/Documentation/Pictures/build10.png)

3.  If not, go back and check if your sensors are connected accordingly.

### Unit Testing - LSM303

For this portion, you will need an internet connection as you will be required
to download libraries in order to test each sensor.

1.  Make sure your Pi is up to date with the latest packages. Run `sudo apt
    update` in the terminal to make sure everything is up to date.

2.  We will be using Python to test the sensors, and will need the appropriate
    tools. Run the following commands in the terminal to ensure we have
    everything required to read from the sensor. We will test the LSM303 first.

    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    sudo apt-get install git build-essential python-dev
    cd ~
    git clone https://github.com/adafruit/Adafruit_Python_LSM303.git
    cd Adafruit_Python_LSM303
    sudo python setup.py install
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

3.  Navigate to the `/Adafruit_Python_LSM303/examples` directory.

4.  Test your sensor by running `sudo python simpletest.py`. Your readings
    should look like the following:

    ![](https://github.com/YamiYukiSenpai/EZTracker/blob/master/Documentation/Pictures/build11.png)

5.  You can test the readings by moving your device in different directions with
    different speeds. You will notice the values changing accordingly.

### Unit Testing - MPR121

1.  Download the test code from Jonas’s repository with:

    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    wget https://raw.githubusercontent.com/YamiYukiSenpai/MacroKeyTouchSensor/master/cap-touch.py
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

2.  Run the code using `sudo python cap-touch.py`. You will then be able to
    touch each of the nodes on the sensor, or you can have individual wires
    running from each position. Below is an example output.
