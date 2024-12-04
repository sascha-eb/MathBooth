# MathBooth

## Checks a timetable for curricular conflicts and room conflicts

Small conflict checker tool packaged with Maven.
Parses a static xml timetable and returns two lists of conflicting entries.

## run the app

### locally
```
mvn exec:java
```

### in docker
```
docker build -t mathbooth .
docker run -it mathbooth
```

