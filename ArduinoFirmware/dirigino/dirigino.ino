#include <Servo.h> 

#define PIN_SERVO 11

#define PIN_ENABLE 5
#define PIN_CTL_1 2
#define PIN_CTL_2 3

Servo tilt;

boolean command_complete = false;
String command_string = "";


void setup() {
  Serial.begin(9600);
  command_string.reserve(64);
  tilt.attach(PIN_SERVO);
  
  pinMode(PIN_ENABLE, OUTPUT);
  pinMode(PIN_CTL_1, OUTPUT);
  pinMode(PIN_CTL_2, OUTPUT);
}

int current_speed = 0;
int current_angle = 90;
boolean current_direction = true;

void loop() {
   analogWrite(PIN_ENABLE, current_speed);
   if (!current_direction) {
     digitalWrite(PIN_CTL_1, HIGH);
     digitalWrite(PIN_CTL_2, LOW);
   }
   else {
     digitalWrite(PIN_CTL_1, LOW);
     digitalWrite(PIN_CTL_2, HIGH);
   }
   
   
   if (command_complete) {
     if (command_string.startsWith("MASTER")) {
       Serial.println("SLAVE");
     }
     else if (command_string.startsWith("TILT")) {
       String degstr = command_string.substring(5);
       int degs = degstr.toInt();
       
       current_direction = (degs < 180);
       current_angle = degs % 180;
       
       tilt.write(current_angle);
       delay(15);
       
       Serial.print("ACK ");
       Serial.println(degs);
     }
     else if (command_string.startsWith("SPEED")) {
       String speedstr = command_string.substring(6);
       int spee = speedstr.toInt();
       current_speed = map(spee, 0, 100, 0, 255);
       Serial.print("ACK ");
       Serial.println(spee);
     }
     else if (command_string.startsWith("STATUS")) {
       Serial.print("ACK speed=");
       Serial.print(map(current_speed, 0, 255, 0, 100));
       Serial.print(" tilt=");
       Serial.println(current_angle);
     }
     else if (command_string.startsWith("RESET")) {
       current_speed = 0;
       current_angle = 90;
       tilt.write(90);
       delay(15);
       Serial.println("ACK");
     }
     else {
       Serial.println("UNKNOWN");
     }
     
     command_complete = false;
     command_string = "";
   }
}

void serialEvent() {
    while (Serial.available()) {
      // get the new byte:
      char inChar = (char)Serial.read(); 
      // add it to the inputString:
      command_string += inChar;
      // if the incoming character is a newline, set a flag
      // so the main loop can do something about it:
      if (inChar == '\n') {
        command_complete = true;
    }
  }
}
