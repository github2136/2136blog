
import java.util.*

/**
 * Created by YB on 2019/11/
 * 路径过滤
 */
class KalmanFilter {

    private var timeStamp: Date? = null // millis
    private var latitude: Double = 0.toDouble() // degree
    private var longitude: Double = 0.toDouble() // degree
    private var variance: Float = 0.toFloat() // P matrix. Initial estimate of error
    private var gps: GPSSingleData? = null//corrected gps data

    init {
        variance = -1f

    }

    fun onLocationUpdate(singleData: GPSSingleData) {
        // if gps receiver is able to return 'accuracy' of position, change last variable
        gps = singleData
        process(singleData.speed, singleData.lat, singleData.lng, singleData.timestamp, singleData.accuracy)
    }

    // Init method (use this after constructor, and before process)
    // if you are using last known data from gps)
    fun setState(latitude: Double, longitude: Double, timeStamp: Date, accuracy: Float) {
        this.latitude = latitude
        this.longitude = longitude
        this.timeStamp = timeStamp
        this.variance = accuracy * accuracy
    }

    /**
     * Kalman filter processing for latitude and longitude
     *
     * newLatitude - new measurement of latitude
     * newLongitude - new measurement of longitude
     * accuracy - measurement of 1 standard deviation error in meters
     * newTimeStamp - time of measurement in millis
     */
    fun process(newSpeed: Float, newLatitude: Double, newLongitude: Double, newTimeStamp: Date, newAccuracy: Float) {
        // Uncomment this, if you are receiving accuracy from your gps
//        val newAccuracy =
//            if (tempAccuracy < 5f) {
//                5f
//            } else {
//                tempAccuracy
//            }
        if (variance < 0) {
            // if variance < 0, object is unitialised, so initialise with current values
            setState(newLatitude, newLongitude, newTimeStamp, newAccuracy)
        } else {
            // else apply Kalman filter
            val duration = newTimeStamp.time - this.timeStamp!!.time
            if (duration > 0) {
                // time has moved on, so the uncertainty in the current position increases
                variance += duration.toFloat() * newSpeed * newSpeed / 1000
                timeStamp = newTimeStamp
            }

            // Kalman gain matrix 'k' = Covariance * Inverse(Covariance + MeasurementVariance)
            // because 'k' is dimensionless,
            // it doesn't matter that variance has different units to latitude and longitude
            val k = variance / (variance + newAccuracy * newAccuracy)
            // apply 'k'
            latitude += k * (newLatitude - latitude)
            longitude += k * (newLongitude - longitude)
            // new Covariance matrix is (IdentityMatrix - k) * Covariance
            variance *= (1 - k)

            // Export new point
            exportNewPoint(newSpeed, longitude, latitude, newTimeStamp, newAccuracy)
        }
    }

    private fun exportNewPoint(speed: Float, longitude: Double, latitude: Double, timestamp: Date, accuracy: Float) {
        val newGPSdata = GPSSingleData(speed, longitude, latitude, timestamp, accuracy)
        gps = newGPSdata
    }

    fun getNewPoint(): GPSSingleData {
        return this.gps!!
    }
}

data class GPSSingleData(val speed: Float, val lng: Double, val lat: Double, val timestamp: Date, val accuracy: Float)