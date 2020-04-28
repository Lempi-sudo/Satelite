@rem In Windows if it cannot find class try both "/" and "\" as path delimiter

@set libloc=.\lib\*

@set idst=.\target\*

java -classpath %Libloc%;%idst% RunGUI

pause

