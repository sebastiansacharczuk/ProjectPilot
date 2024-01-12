package com.sebsach.projectpilot.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sebsach.projectpilot.ui.theme.ProjectPilotTheme

/**
 * @author Sebastian Sacharczuk
 * @github https://github.com/sebastiansacharczuk
 */
class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("MainActivity: onCreate")
        setContent {
            ProjectPilotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


//                    val sections = listOf(
//                        CollapsableSection(
//                            title = "My Projects",
//                            rows = listOf("Project1", "Project2", "Project3")
//                        ),
//                        CollapsableSection(
//                            title = "Requests to join",
//                            rows = listOf("Project4", "Project5")
//                        )
//                    )
//                    CollapsableLazyColumn(sections = sections)
                }
            }
        }
    }
}

//@Composable
//fun CollapsableLazyColumn(
//    sections: List<CollapsableSection>,
//    modifier: Modifier = Modifier
//) {
//    val collapsedState = remember(sections) { sections.map { true }.toMutableStateList() }
//
//    LazyColumn(modifier) {
//        sections.forEachIndexed { i, dataItem ->
//            val collapsed = collapsedState[i]
//            item(key = "header_$i") {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier
//                        .clickable {
//                            collapsedState[i] = !collapsed
//                        }
//                ) {
//                    Icon(
//                        Icons.Default.run { if (collapsed) KeyboardArrowDown else KeyboardArrowUp },
//                        contentDescription = "",
//                        tint = Color.LightGray,
//                    )
//                    Text(
//                        dataItem.title,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier
//                            .padding(vertical = 10.dp)
//                            .weight(1f)
//                    )
//                }
//                Divider()
//            }
//            if (!collapsed) {
//                items(dataItem.rows) { row ->
//                    Button(onClick = { /* Handle button click */ }) {
//                        Text(row)
//                    }
//                }
//            }
//        }
//    }
//}
//data class CollapsableSection(val title: String, val rows: List<String>)
