## Airship design
* TODO: Define general structural design

## Equipment
### Controllers
* Arduino UNO
* Smartphone

Arduino provides basic control for the movement of the airship. It
might be directly connected to some sensors for safety of navigation.

The Smartphone provides remote control capability, and provides some
sensors.

### Sensors
* GPS
* Tilt Sensor
* Accelerometer (3-axis)
* Downwards Camera
* Front Camera
* (Optional) Temperature / Barometric Sensor


GPS is used to determine the position of the airship, including its
altitude, and provide navigational support.

Tilt Sensor(s) are used to detect an abnormal position of the airship,
e.g., upside-down or side-ways. 
Together with the Accelerometer this information is important in order
to guarantee that the engines provide thrust in the desired direction.

The cameras are use to take pictures of the landscape. The front
camera can also be used to avoid direct collisions.

Temperature / Barometric Sensors can provide detail data on the pull
provided by the environment to the airship, e.g., low-pressure means
less pull; this can be counter-acted by using the engines to restore
balance.


### Actuators
* 2 Servos (180 deg)
* 2 Engines

The engines are fixed on the servos, providing rotation of the engines
to up-to 180 deg.

The engines is attached to a fan [[Define what kind of fans: size,
speed etc.]].

### PSU
* 9V - Arduino power-supply
* Engine power-supply