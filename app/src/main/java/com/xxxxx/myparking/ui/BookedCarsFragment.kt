package com.xxxxx.myparking.ui

import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xxxxx.myparking.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.contacts_fragment.*
import kotlinx.android.synthetic.main.item_contact.view.*

class BookedCarsFragment: Fragment() {
    /*
    private val args: BookedCarsFragment by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.contacts_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recoverContactData()
    }

    private fun recoverContactData () {

        val contactList = mutableListOf<Contact>()

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        )

        val contacts = context!!.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        contacts?.let { cursor ->
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {

                    val contact = Contact (
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) ?: "",
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) ?: "",
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)) ?: "",
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)) ?: ""
                    )

                    contactList.add(contact)
                }
            }
        }
        contacts?.close()

        contacts_list.apply {
            this.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            this.adapter = ContactsAdapter(contactList){
                findNavController()
            }
        }


    }

    internal class ContactsAdapter(private val contactList:List<Contact>,
                                   private val elementClick: (Contact)-> Unit): RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact,parent,false)
            return ContactViewHolder(view)
        }

        override fun getItemCount(): Int {
            return contactList.count()
        }

        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
            val contact = contactList.get(position)
            holder.bindContact(contact)
            holder.containerView.setOnClickListener{elementClick(contact)}
        }

        class ContactViewHolder(override val containerView:View):RecyclerView.ViewHolder(containerView),LayoutContainer{
            fun bindContact(contact:Contact){
                containerView.contactName.text= contact.name
                containerView.contactPhone.text= contact.phoneNumber
            }
        }
    }*/

}

@Parcelize
data class Contact (
    val name: String,
    val phoneNumber: String,
    val contactId: String,
    val photoUri: String
): Parcelable