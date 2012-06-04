JC = javac
JFLAGS = 
CLASSES = 			\
	ActionImpl.java		\
	CarImpl.java		\
	LearningModuleImpl.java	\
	Main.java		\
	RoadMapImpl.java	\
	TrafficLightImpl.java	\
	Viewer.java

default:
	$(JC) $(JFLAGS) $(CLASSES)

clean:
	find . | grep "class$$" | xargs rm -f
