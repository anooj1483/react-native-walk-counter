
# react-native-walk-counter

## Getting started

`$ npm install react-native-walk-counter --save`

### Mostly automatic installation

`$ react-native link react-native-walk-counter`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-walk-counter` and add `RNWalkCounter.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNWalkCounter.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNWalkCounterPackage;` to the imports at the top of the file
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

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNWalkCounter.sln` in `node_modules/react-native-walk-counter/windows/RNWalkCounter.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Walk.Counter.RNWalkCounter;` to the usings at the top of the file
  - Add `new RNWalkCounterPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNWalkCounter from 'react-native-walk-counter';

// TODO: What to do with the module?
RNWalkCounter;
```
  