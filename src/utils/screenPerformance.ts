import perf from '@react-native-firebase/perf';

export const startTrace = async (traceName) => {
  const trace = perf().newTrace(traceName)
  await trace.start()
  return trace
}

export const stopTrace = async (trace) => {
  await trace.stop()
}

export const logMeasurement = async (
  trace,
  id,
  phase,
  actualDuration,
  baseDuration
) => {
  if (__DEV__)
    console.log({
      id,
      phase,
      actualDuration,
      baseDuration,
    })
  if (phase === "mount") {
    await trace.putAttribute("mount_time", actualDuration)
  } else if (phase === "update") {
    await trace.incrementMetric("updates", 1)
  }
}
