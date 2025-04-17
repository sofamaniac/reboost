/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.subreddit

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.sofamaniac.reboost.AppDatabase
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.subreddit.SubredditEntity
import com.sofamaniac.reboost.reddit.subreddit.SubredditName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.graphics.toColorInt

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SubredditIcon(subreddit: SubredditName, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val dao = db.subredditDao()
    val entity by produceState<SubredditEntity?>(initialValue = null, subreddit) {
        value = withContext(Dispatchers.IO) {
            var temp = dao.getByName(subreddit)
            if (temp?.iconImg.isNullOrBlank()) {
                Log.d("SubredditIcon", "Not found in DB (${subreddit.name})")
                val res = RedditAPI.service.getSubInfo(subreddit)
                if (res.isSuccessful) {
                    val entity = SubredditEntity.fromSubreddit(res.body()!!)
                    Log.d("SubredditIcon", "Inserted into DB (${entity})")
                    dao.insert(entity)
                    temp = entity
                }
            } else {
                Log.d("SubredditIcon", "Found in DB (${subreddit.name}) : ${temp?.iconImg}")
            }
            temp
        }
    }
    if (!entity?.iconImg.isNullOrBlank()) {
        GlideImage(
            model = entity!!.iconImg,
            contentDescription = "${subreddit.name} icon",
            modifier
        )
    } else if (!entity?.primaryColor.isNullOrBlank()) {
        Box(
            modifier = modifier
                .background(
                    Color(entity!!.primaryColor.toColorInt()),
                    shape = CircleShape
                )
                .border(width = 1.dp, color = Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("r")
        }
    } else {
        Box(modifier = modifier.background(Color.Gray, shape = CircleShape))
    }
}