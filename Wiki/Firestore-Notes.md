## Purpose
This section of the wiki is being created as a tool to help us learn about and better understand Firestore. Additionally, this will likely become a quick reference for us to be able to use while developing with firestore for this application.

***

## To Be Stored
- User(uuid, udid, first name, last name, isOwner)
- QRCode(id, codeVal)
- QRComment(id, QRid, comment)
- Photo(id, photo: byte_type)
---

## From [Firestore Data Model](https://firebase.google.com/docs/firestore/data-model):

  - Cloud Firestore is optimized for storing ***large collections*** of ***small documents***.
    - Each document contains a set of key-value pairs.
    - If either the collection or document does not exist, Cloud Firestore creates it.


### Documents
  - Simple document: 
      ```
      📄 alovelace
        first : "Ada"
        last : "Lovelace"
        born : 1815
      ```
  - Nested document:
      ```
      📄 alovelace
        name :
          first : "Ada"
          last : "Lovelace"
        born : 1815
      ```


### Collections

Collection = 🗂  
Document = 📄
```
🗂 users
    📄 alovelace
        first : "Ada"
        last : "Lovelace"
        born : 1815

    📄 aturing
        first : "Alan"
        last : "Turing"
        born : 1912
```


### References

  - You can create a reference whether or not data exists there.
  - Creating a reference does not perform any network operations.


#### Reference to document

```java
DocumentReference alovelaceDocumentRef = db.collection("users").document("alovelace");
```
For simplicity, documents can be referenced by path too:
```java
DocumentReference alovelaceDocumentRef = db.document("users/alovelace");
```

#### Reference to collection

```java
CollectionReference usersCollectionRef = db.collection("users");
```


### Subcollections

You can create a subcollection called messages for every room document in your rooms collection:
```
🗂 rooms

    📄 roomA

    name : "my chat room"

        🗂 messages

            📄 message1

            from : "alex"
            msg : "Hello World!"

            📄 message2

            ...

    📄 roomB

    ...
```
 - ***Deleting a document does not delete its subcollections!***

## [Snapshot Listeners](https://firebase.google.com/docs/firestore/query-data/listen)
```java
final DocumentReference docRef = db.collection("cities").document("SF");
docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
    @Override
    public void onEvent(@Nullable DocumentSnapshot snapshot,
                        @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "Listen failed.", e);
            return;
        }

        if (snapshot != null && snapshot.exists()) {
            Log.d(TAG, "Current data: " + snapshot.getData());
        } else {
            Log.d(TAG, "Current data: null");
        }
    }
});
```
  - A document can be watched for updates.
  - On an update, the registered handler will be run.


## Questions

  - Does Firebase use a one-way or two-way connection? 
    - Can Firebase send unprompted data updates, and if so, how do we implement registration of handlers?
    - **Answer:** [Firebase can utilize a two-way connection.](https://firebase.google.com/docs/firestore/query-data/listen)


## Design

  - [FirebaseUI](https://firebaseopensource.com/projects/firebase/firebaseui-android/firestore/readme/)
    - Could use this instead of our own classes. Provides views and adapters.
  - ArrayAdapter:
    - [Uses *observer class* to watch it's items for updates.](https://stackoverflow.com/a/59188221)
  - Firebaseable objects
    - Each Firebaseable object is responsible for registering itself for updates.
 

## Solution 1
![FSData UML](https://github.com/CMPUT301W22T36/CodeHunters/blob/main/doc/Static/FSData.svg)

```java
// FSData is a base class for any class storing data on Firestore.
// Lists should keep a collection of such subclasses, and use an Observer to 
// check for updates.

class FSData extends observable {
    private string path = null;

    public FSData(string path) {
        // path: The path to the document (including collection in the path, ex. "collection/document")
        this.path = path;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.document(path);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists() && !this.checkErrors(snapshot, e)) {
                    this.handleUpdate(snapshot);  // Shouldn't be null at this point.
                }
            }
        });
        
    }

    private bool checkErrors(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
        // Used to check errors when database sends update.
        if (e != null) {
            return true;
        }
        return false;
    }

    private void handleUpdate(DocumentSnapshot snapshot);  // Used to handle updates from database.

    public void get() {
        this.setChanged();
        this.notifyObservers();
    }
    public void post();
    public void put();
    public void delete();
}
```

All classes that want to store data in Firestore should inherit from this base class. These subclasses would be solely responsible for handling updates recieved from Firestore (and sending updates to Firestore).


- Data subclasses would handle everything. Very clean solution. (loose coupling)
- Override-able updateHandler provides an easy way for subclasses to modify attributes. Behind an error guard as well, so errors won't need to be handled.
- If for some reason errors do need to be checked in a specialized manner, there is an override-able errorChecker method.
- Observable


### List of FSData
- Could be a generic class.
- (maybe?) implements some array datatype so it is easily operated on with [] brackets.
- Method for querying FSData
```java
class FSList implements Observer {...}
```

---
## Needs:

- Store simple object (no nested lists)
- Query objects in a collection
- Automatically update simple objects when database is updated
- Maintain a list of simple objects


## Solution 2

- Use POD (POJO) for everything
- One central Firestore manager (singleton) for adding, updating, or deleting all objects
- POD can be put straight into the Firestore manager, no need for fancy serialization methods
- The Firestore manager should be used to register objects to be updated when their remote instance in the database changes

```java
class PODExample {
    private int x;
    
    int getX() { return x; }
    void setX(int x) { this.x = x; }
}
```
---
## Applicable Design Patterns
- DAO DTO
- *Table Data Gateway*  (Business Object Mapper)

![Table Data Gateway](https://github.com/CMPUT301W22T36/CodeHunters/blob/main/doc/Static/TDG_UML.svg)