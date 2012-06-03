xJFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Action.java \
	Car.java \
	LearningModule.java \
	RoadMap.java \
	TrafficLight.java \
	Coords.java \
	Velocity.java \
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
