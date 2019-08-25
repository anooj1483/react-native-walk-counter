
import { DeviceEventEmitter,
    NativeAppEventEmitter,
    NativeEventEmitter,
    NativeModules,
    Platform, } from 'react-native';

const { RNWalkCounter } = NativeModules;
//const Emitter = new NativeEventEmitter(RNWalkCounter);

export default RNWalkCounter;
