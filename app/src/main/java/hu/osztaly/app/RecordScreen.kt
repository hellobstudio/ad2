package hu.osztaly.app

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecordScreen(repo: Repo, modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    var topic by rememberSaveable { mutableStateOf("") }
    val marks = remember { mutableStateMapOf<String, Boolean>() }

    Column(modifier.fillMaxSize().padding(12.dp)) {
        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it },
            label = { Text("Téma (pl. jelenlét, újságigénylés)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        if (repo.students.isEmpty()) {
            Text("Nincs diák. Importáld őket a Beállítások fülön.")
        }

        LazyColumn(Modifier.weight(1f)) {
            items(repo.students, key = { it.name }) { s ->
                val on = marks[s.name] == true
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(s.name, Modifier.weight(1f), fontSize = 18.sp)
                    Box(
                        Modifier
                            .width(72.dp)
                            .height(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (on) GreenCell else RedCell)
                            .clickable { marks[s.name] = !on },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            if (on) "+" else "−",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                HorizontalDivider()
            }
        }

        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                when {
                    repo.students.isEmpty() ->
                        Toast.makeText(ctx, "Előbb importálj diákokat.", Toast.LENGTH_SHORT).show()
                    topic.isBlank() ->
                        Toast.makeText(ctx, "Add meg a rögzítés témáját.", Toast.LENGTH_SHORT).show()
                    else -> {
                        repo.addRecord(topic.trim(), marks.toMap())
                        topic = ""
                        marks.clear()
                        Toast.makeText(ctx, "Mentve.", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Mentés", fontSize = 18.sp)
        }
    }
}
