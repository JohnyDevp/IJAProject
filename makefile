pack:
	rm -rf build;\
	rm -rf lib;\
	rm -rf dest;\
	rm -rf doc;\
	mkdir build;\
	mkdir dest;\
	mkdir doc;\
	mkdir lib;\
	zip -r  xzimol04.zip build data dest doc src pom.xml readme.txt requirements.pdf lib;