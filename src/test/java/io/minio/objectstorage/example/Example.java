/*
 * Minimal Object Storage Library, (C) 2015 Minio, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.minio.objectstorage.example;

import com.google.api.client.util.IOUtils;
import io.minio.objectstorage.client.Client;
import io.minio.objectstorage.client.ObjectMetadata;
import io.minio.objectstorage.client.errors.ObjectStorageException;
import io.minio.objectstorage.client.messages.Item;
import io.minio.objectstorage.client.messages.ListAllMyBucketsResult;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class Example {
    public static void main(String[] args) throws IOException, XmlPullParserException, ObjectStorageException {
        System.out.println("Example app");

        // play.minio.io - s3 compatible object storage
        Client client = Client.getClient("http://play.minio.io:9000");
        // Requires no credentials for play.minio.io

        // amazonaws.com - amazon s3 object storage
//         Client client = Client.getClient("https://s3.amazonaws.com");
//         client.setKeys("accessKey", "secretKey");

        // s3-us-west-2 - amazon s3 object storage in oregon
//        Client client = Client.getClient("https://s3-us-west-2.amazonaws.com");
//        client.setKeys("accessKey", "secretKey");

        // Set a user agent for your app
        client.setUserAgent("Example app/ (0.1)");

        // create bucket
        client.makeBucket("mybucket", Client.ACL_PUBLIC_READ_WRITE);

        // set bucket ACL
        client.setBucketACL("mybucket", Client.ACL_PRIVATE);

        // list buckets
        ListAllMyBucketsResult allMyBucketsResult = client.listBuckets();
        System.out.println(allMyBucketsResult);

        // create object
        client.putObject("mybucket", "myobject", "application/octet-stream", 11, new ByteArrayInputStream("hello world".getBytes("UTF-8")));

        // list objects
        Iterator<Item> myObjects = client.listObjectsInBucket("mybucket");
        System.out.println(myObjects);

        // get object metadata
        ObjectMetadata objectMetadata = client.getObjectMetadata("mybucket", "myobject");
        System.out.println(objectMetadata);

        // get object
        InputStream object = client.getObject("mybucket", "myobject");
        try {
            System.out.println("Printing object: ");
            IOUtils.copy(object, System.out);
        } finally {
            object.close();
        }

        client.deleteObject("mybucket", "myobject");
    }
}