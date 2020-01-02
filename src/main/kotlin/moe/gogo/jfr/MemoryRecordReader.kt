package moe.gogo.jfr

import com.jrockit.mc.flightrecorder.FlightRecordingLoader
import com.jrockit.mc.flightrecorder.spi.IEvent
import moe.gogo.MemoryRecord
import moe.gogo.HeapSample
import java.io.File
import java.time.Instant

class MemoryRecordReader(val jfr: File) {

    fun load(): MemoryRecord {
        val recording = FlightRecordingLoader.loadFile(jfr)

        val view = recording.createView()
        view.setFilter { it.eventType.name == "Heap Summary" }

        val events = mutableListOf<IEvent>()
        view.forEach { events.add(it) }
        events.sortBy { it.startTimestamp }

        val recordList = events.map {
            val time = it.getValue("(startTime)") as Long
            HeapSample(
                Instant.ofEpochSecond(time / 1000000000L, time % 1000000000L),
                it.getValue("heapSpace:committedSize") as Long,
                it.getValue("heapUsed") as Long
            )
        }

        return MemoryRecord(recordList)
    }

}
