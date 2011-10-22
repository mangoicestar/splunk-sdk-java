/*
 * Copyright 2011 Splunk, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"): you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import com.splunk.atom.*;
import com.splunk.http.ResponseMessage;
import com.splunk.Args;
import com.splunk.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Iterator;

public class Entity extends Resource {
    Map<String, Object> content;
    String title;

    public Entity(Service service, String path) {
        super(service, path);
    }

    public ResponseMessage get() throws IOException {
        return service.get(path);
    }

    public void disable() {
        invoke("disable");
        invalidate();
    }

    public void enable() {
        invoke("enable");
        invalidate();
    }

    Map<String, Object> getContent() {
        validate();
        return this.content;
    }

    public String getTitle() {
        validate();
        return this.title;
    }

    void load(AtomEntry entry) {
        super.load(entry);
        this.content = entry.content;
        this.title = entry.title;
    }

    public void update(Args args) {
        invoke("udpate", args);
        invalidate();
    }

    public void refresh() {
        ResponseMessage response;
        try {
            response = get();
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        assert(response.getStatus() == 200); // UNDONE
        AtomFeed feed = AtomFeed.create(response.getContent());
        assert(feed.entries.size() == 1);
        AtomEntry entry = feed.entries.get(0);
        load(entry);
    }

    public void remove() {
        invoke("remove");
        // UNDONE: would like to set maybe = false on container
    }
}
