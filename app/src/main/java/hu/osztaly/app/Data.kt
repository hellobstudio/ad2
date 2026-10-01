package hu.osztaly.app

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.json.JSONArray
import org.json.JSONObject
import java.text.Collator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Student(val name: String, val note: String = "")
data class Entry(val name: String, val value: Boolean)
data class Record(val topic: String, val date: String, val entries: List<Entry>)

class Repo(context: Context) {
    private val prefs = context.getSharedPreferences("osztaly", Context.MODE_PRIVATE)

    val students = mutableStateListOf<Student>()
    val records = mutableStateListOf<Record>()
    var darkTheme by mutableStateOf(prefs.getBoolean("dark", false))
        private set

    init {
        load()
    }

    private fun load() {
        try {
            val sa = JSONArray(prefs.getString("students", "[]"))
            for (i in 0 until sa.length()) {
                val o = sa.getJSONObject(i)
                students.add(Student(o.getString("name"), o.optString("note", "")))
            }
            val ra = JSONArray(prefs.getString("records", "[]"))
            for (i in 0 until ra.length()) {
                val o = ra.getJSONObject(i)
                val ea = o.getJSONArray("entries")
                val list = (0 until ea.length()).map { j ->
                    val e = ea.getJSONObject(j)
                    Entry(e.getString("n"), e.getBoolean("v"))
                }
                records.add(Record(o.getString("topic"), o.getString("date"), list))
            }
        } catch (e: Exception) {
            // hibás adat esetén üresen indul
        }
    }

    private fun persist() {
        val sa = JSONArray()
        students.forEach { sa.put(JSONObject().put("name", it.name).put("note", it.note)) }
        val ra = JSONArray()
        records.forEach { r ->
            val ea = JSONArray()
            r.entries.forEach { ea.put(JSONObject().put("n", it.name).put("v", it.value)) }
            ra.put(JSONObject().put("topic", r.topic).put("date", r.date).put("entries", ea))
        }
        prefs.edit()
            .putString("students", sa.toString())
            .putString("records", ra.toString())
            .apply()
    }

    fun setDark(value: Boolean) {
        darkTheme = value
        prefs.edit().putBoolean("dark", value).apply()
    }

    /** Soronként egy név. Visszaadja a hozzáadott diákok számát. */
    fun addStudents(text: String): Int {
        val have = students.map { it.name }.toMutableSet()
        var added = 0
        text.replace("\uFEFF", "").lines().map { it.trim() }.filter { it.isNotEmpty() }.forEach { n ->
            if (have.add(n)) {
                students.add(Student(n))
                added++
            }
        }
        val collator = Collator.getInstance(Locale("hu", "HU"))
        students.sortWith { a, b -> collator.compare(a.name, b.name) }
        persist()
        return added
    }

    fun removeStudent(index: Int) {
        if (index in students.indices) {
            students.removeAt(index)
            persist()
        }
    }

    fun setNote(name: String, note: String) {
        val i = students.indexOfFirst { it.name == name }
        if (i >= 0) {
            students[i] = students[i].copy(note = note)
            persist()
        }
    }

    fun addRecord(topic: String, marks: Map<String, Boolean>) {
        val date = SimpleDateFormat("yyyy.MM.dd. HH:mm", Locale("hu", "HU")).format(Date())
        records.add(Record(topic, date, students.map { Entry(it.name, marks[it.name] == true) }))
        persist()
    }

    fun removeRecord(index: Int) {
        if (index in records.indices) {
            records.removeAt(index)
            persist()
        }
    }

    fun clearRecords() {
        records.clear()
        persist()
    }
}
