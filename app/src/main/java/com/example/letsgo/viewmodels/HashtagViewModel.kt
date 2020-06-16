package com.example.letsgo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.letsgo.data.DataAccessObject
import com.example.letsgo.models.Hashtag
import com.example.letsgo.utilities.HashtagUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HashtagViewModel(private val dao: DataAccessObject<Hashtag>) : ViewModel() {



    fun createHashtag(hashtag: String) : Hashtag {

        // if hashtag is invalid, throw an error
        if(!HashtagUtils.verifyHashtag(hashtag))
            throw Error()

        // set parent ID to non-existent for now
        var parentID: Int = 0

        //if this hashtag is a child of another, check if it exists
        val parentName = HashtagUtils.getPath(hashtag)
        if(!parentName.equals("")){

            val parentTag: Hashtag? = fetchHashtagByName(parentName)

            // set parentID to this tag if it exists, otherwise create it
            parentID = parentTag?.id ?: createHashtag(parentName).id

        }

        // create hashtag and insert into database (sets of child IDs and task IDs will be empty)
        val creation = Hashtag(hashtag, parentID, HashSet(), HashSet())
        dao.insert(creation)

        // if this tag has a parent, add this tag as a child and update the parent (WHICH MUST NECESSARILY EXIST NOW)
        if(parentID != 0){

            val parentTag: Hashtag = fetchHashtagByID(parentID)!!
            parentTag.addChild(creation.id)
            dao.update(parentTag)

        }

        return creation

    }

    fun fetchHashtagByID(id: Int) : Hashtag? = dao.fetchByID(id).value
    fun fetchHashtagByName(name: String) : Hashtag? = dao.fetchByQuery(SimpleSQLiteQuery("SELECT * FROM ${Hashtag.hashtagTable} WHERE name = $name")).value

    fun tagTask(hashtagID: Int, taskID: Int) {

    

    }

}