# File Store

The file store service is responsible for storing files uploaded by a user.  The file is accesssible to any user in the same "org".

Additionally, the file may be accessed by internal system processes or analytics.

Derived products, such as annotations extracted from an image, are associated with the same user credentials and have the same user permissions.

The file service serves to
* Isolate storage from processing/analytics
* Stream files back to the frontend (e.g. video player)
* Provides a URL for external services (such as azure transcription)

# Version History
  - 0.1 as of 20240627

# Methods
* Upload
  - Upload a file to the given UUID
  - The response body as String contains a URL pointing to the file
* Stream (video)
  * supports streaming the video to a javascript viewer, with ranges
* Stream Audio
  * supports streaming the audio to a javascript widget, with ranges
* Stream v2
  * TODO: what is the difference? 
* listFiles
  * gets all the files associated with this UUID, which includes derived files, such as the audio track for a video, or the transcript of the audio
* getFileName
  * gets the filename for this uuid, fails if more than one file
* getMimeType
  * gets the mime type for the filename, 
* getMetadata

# Streaming
The streaming APIs support the normal http streaming and range capabilities for loading through browsers, and can also be utilized to get the whole payload of the file.

# TODO
  * Remove the deprecated v1 APIs
  * Modify the paths to have consistent values
  * Support other file formats than video and audio
  * Support additional properties such as expiry on the file
    * File can only be stored for a limited time, and deleted after that, with the assumption processing and such are completed by that date, and the original file is held permanently by the customer.