xJFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	interfaces\Action.java \
	interfaces\Car.java \
	interfaces\LearningModule.java \
	interfaces\RoadMap.java \
	interfaces\TrafficLight.java \
	utils\Coords.java \
	utils\Velocity.java \
        ActionImpl.java \
        CarImpl.java \
        LearningModuleImpl.java \
	RoadMapImpl.java \
	TrafficLightImpl.java \
	Viewer.java \
        Main.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
