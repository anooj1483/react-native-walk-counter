
# react-native-walk-counter

## Getting started

`$ npm install react-native-walk-counter --save`

### Mostly automatic installation

`$ react-native link react-native-walk-counter`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.synclovis.RNWalkCounterPackage;` to the imports at the top of the file
  - Add `new RNWalkCounterPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-walk-counter'
  	project(':react-native-walk-counter').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-walk-counter/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-walk-counter')
  	```


## Usage
```javascript
import RNWalkCounter from 'react-native-walk-counter';

const WalkEvent = new NativeEventEmitter(RNWalkCounter)

componentDidMount(){
	
	WalkEvent.addListener('onStepRunning', (event) => {      
      console.log(event.steps)      
	})
	   
   	RNWalkCounter.startCounter()
}

componentWillUnmount(){
	RNWalkCounter.stopCounter()
	WalkEvent.removeListener('onStepRunning')	
}

```
  