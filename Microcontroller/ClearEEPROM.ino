/********************************************************************************************

                       EEPROM-Cleaner v1.2.0

                       Copyright (c) 2017 Helmut Stult (schinfo)

 ********************************************************************************************/

#include <EEPROM.h>
#include <ESP8266WiFi.h>

// sizeBytes being the number of bytes you want to use.
// It's defined with "#define sizeBytes"
// Size can be anywhere between 4 and 4096 bytes (Default for ESP8266_deauther = 4096)
#define sizeBytes 4096

// change it for lower or higher endByte (Default for ESP8266_deauther = 4096)
// normaly it's the same as sizeBytes
#define endByte 4096

// change it for lower or higher startByte (Default = 0)
#define startByte 0

unsigned long ok = 0;
unsigned long nok = 0;
unsigned long tok = 0;


void setup()
{
  Serial.begin(115200);
  EEPROM.begin(sizeBytes);

  delay(100);

  Serial.println("**********************************************************************************************************");
  Serial.println("");
  Serial.print("    Write a char(255) / hex(FF) from byte ");
  Serial.print(startByte);
  Serial.print(" to ");
  Serial.print(endByte - 1);
  Serial.print(" into the EEPROM with a defined size of ");
  Serial.print("");
  Serial.print(sizeBytes);
  Serial.println(" Bytes");
  Serial.println("");
  Serial.println("**********************************************************************************************************");
  Serial.println("");

  Serial.println("             testing EEPROM for written bytes");
  Serial.println("");

  for (int i = startByte; i < endByte; ++i)
  {
    if (EEPROM.read(i) == 255) {
      ++ok;
    } else {
      ++nok;
    }
  }

  Serial.printf("               empty bytes: %6d\r\n", ok);
  Serial.printf("           not empty bytes: %6d\r\n", nok);
  Serial.println("");
  Serial.println("**********************************************************************************************************");
  Serial.println("");

  Serial.println("**********************************************************************************************************");
  Serial.println("");
  Serial.println("             Start clearing EEPROM... - Please wait!!!");
  Serial.println("");
  Serial.println("**********************************************************************************************************");

  delay(1000);

  // write a char(255) / hex(FF) from startByte until endByte into the EEPROM
  for (int i = startByte; i < endByte; ++i) {
    EEPROM.write(i, -1);
  }

  EEPROM.commit();

  delay(1000);

  Serial.println("");
  Serial.println("             testing EEPROM for clearing");
  Serial.println("");

  String test;
  for (int i = startByte; i < endByte; ++i)
  {
    if (EEPROM.read(i) == 255) {
      ++tok;
    }
  }
  Serial.println("**********************************************************************************************************");
  Serial.println("");
  if (tok = endByte - startByte) {
    Serial.println("             EEPROM killed correctly");
  } else
    Serial.println("             EEPROM not killed - ERROR !!!");

  Serial.println("");
  Serial.println("**********************************************************************************************************");
  Serial.println("");
  Serial.println("             Ready - You can remove your ESP8266 / LoLin");
  Serial.println("");
  Serial.println("**********************************************************************************************************");
}

void loop()
{
}