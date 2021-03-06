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

package com.splunk;

import org.junit.Test;

public class NamespaceTest extends SDKTestCase {
    @Test
    public void testStaticNamespace() {
        assertEquals(
                "This test is not valid when Service owner is overridden.",
                null, service.getOwner());
        assertEquals(
                "This test is not valid when Service app is overridden.",
                null, service.getApp());
        
        Args namespace = new Args();

        // syntactic tests
        namespace.clear();
        assertEquals("/services/",
            service.fullpath("", null));

        namespace.clear();
        namespace.put("owner", "Bob");
        assertEquals("/servicesNS/Bob/-/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("app", "search");
        assertEquals("/servicesNS/-/search/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("app", "search");
        namespace.put("owner", "Bob");
        assertEquals("/servicesNS/Bob/search/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "user");
        assertEquals("/servicesNS/-/-/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "user");
        namespace.put("owner", "Bob");
        assertEquals("/servicesNS/Bob/-/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "user");
        namespace.put("app", "search");
        assertEquals("/servicesNS/-/search/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "user");
        namespace.put("app", "search");
        namespace.put("owner", "Bob");
        assertEquals("/servicesNS/Bob/search/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "app");
        assertEquals("/servicesNS/nobody/-/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "app");
        namespace.put("owner", "Bob");
        assertEquals("/servicesNS/nobody/-/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "app");
        namespace.put("app", "search");
        assertEquals("/servicesNS/nobody/search/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "app");
        namespace.put("app", "search");
        namespace.put("owner", "Bob");
        assertEquals("/servicesNS/nobody/search/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "global");
        assertEquals("/servicesNS/nobody/-/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "global");
        namespace.put("owner", "Bob");
        assertEquals("/servicesNS/nobody/-/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "global");
        namespace.put("app", "search");
        assertEquals("/servicesNS/nobody/search/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "global");
        namespace.put("app", "search");
        namespace.put("owner", "Bob");
        assertEquals("/servicesNS/nobody/search/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "system");
        assertEquals("/servicesNS/nobody/system/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "system");
        namespace.put("owner", "Bob");
        assertEquals("/servicesNS/nobody/system/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "system");
        namespace.put("app", "search");
        assertEquals("/servicesNS/nobody/system/",
            service.fullpath("", namespace));

        namespace.clear();
        namespace.put("sharing", "system");
        namespace.put("app", "search");
        namespace.put("owner", "Bob");
        assertEquals("/servicesNS/nobody/system/",
            service.fullpath("", namespace));
    }

    @Test
    public void testLiveNamespace1() throws Exception {
        String username = "sdk-user";
        String password = "changeme";
        String savedSearch = "sdk-test1";
        String searchString = "search index=main * | head 10";

        // Setup a namespace
        Args namespace = new Args();
        namespace.put("owner", username);
        namespace.put("app", "search");

        // get all users, scrub and make our test user
        UserCollection users = service.getUsers();
        if (users.containsKey(username))
            users.remove(username);
        assertFalse(users.containsKey(username));
        users.create(username, password, "user");
        assertTrue(users.containsKey(username));

        // get saved searches for our new namespace, clean to make sure
        // we remove, before we create one.
        SavedSearchCollection savedSearches =
            service.getSavedSearches(namespace);

        if (savedSearches.containsKey(savedSearch))
            savedSearches.remove(savedSearch);
        assertFalse(savedSearches.containsKey(savedSearch));

        savedSearches.create(savedSearch, searchString);
        assertTrue(savedSearches.containsKey(savedSearch));

        // remove saved search
        savedSearches.remove(savedSearch);
        assertFalse(savedSearches.containsKey(savedSearch));

        // remove user
        users.remove(username);
        assertFalse(users.containsKey(username));
    }

    @Test
    public void testLiveNamespace2() throws Exception {

        /* establish naming convention for separate namespaces */
        String search = "search *";

        String searchName11 = "sdk-test-search11";
        String searchName12 = "sdk-test-search12";
        String searchName21 = "sdk-test-search21";
        String searchName22 = "sdk-test-search22";

        String username1 = "sdk-user1";
        String username2 = "sdk-user2";
        String appname1 = "sdk-app1";
        String appname2 = "sdk-app2";

        Args namespace11 = new Args();
        Args namespace12 = new Args();
        Args namespace21 = new Args();
        Args namespace22 = new Args();
        Args namespacex1  = new Args();
        Args namespaceNobody1 = new Args();
        Args namespaceBad = new Args();

        namespace11.put("owner", username1);
        namespace11.put("app",  appname1);
        namespace12.put("owner", username1);
        namespace12.put("app",  appname2);
        namespace21.put("owner", username2);
        namespace21.put("app",  appname1);
        namespace22.put("owner", username2);
        namespace22.put("app",  appname2);
        namespacex1.put("owner", "-");
        namespacex1.put("app", appname1);
        namespaceNobody1.put("owner", "nobody");
        namespaceNobody1.put("app", appname1);
        namespaceBad.put("owner", "magilicuddy");
        namespaceBad.put("app",  "oneBadApp");

        /* scrub to make sure apps don't already exist */
        EntityCollection<Application> apps = service.getApplications();
        if (apps.containsKey(appname1)) {
            apps.remove(appname1);
            clearRestartMessage();
            apps = service.getApplications();
        }
        if (apps.containsKey(appname2)) {
            apps.remove(appname2);
            clearRestartMessage();
            apps = service.getApplications();
        }
        assertFalse(apps.containsKey(appname1));
        assertFalse(apps.containsKey(appname2));

        /* scrub to make sure users don't already exist */
        UserCollection users = service.getUsers();
        if (users.containsKey(username1))
            users.remove(username1);
        if (users.containsKey(username2))
            users.remove(username2);
        assertFalse(users.containsKey(username1));
        assertFalse(users.containsKey(username2));

        /* create users */
        users.create(username1, "abc", "user");
        users.create(username2, "abc", "user");
        assertTrue(users.containsKey(username1));
        assertTrue(users.containsKey(username2));

        /* create apps */
        apps.create(appname1);
        apps.create(appname2);
        assertTrue(apps.containsKey(appname1));
        assertTrue(apps.containsKey(appname2));

        /* create namespace specfic UNIQUE searches */
        SavedSearchCollection
            savedSearches11 = service.getSavedSearches(namespace11);
        SavedSearchCollection
            savedSearches12 = service.getSavedSearches(namespace12);
        SavedSearchCollection
            savedSearches21 = service.getSavedSearches(namespace21);
        SavedSearchCollection
            savedSearches22 = service.getSavedSearches(namespace22);
        SavedSearchCollection
            savedSearchesx1 = service.getSavedSearches(namespacex1);
        SavedSearchCollection
            savedSearchesNobody1 = service.getSavedSearches(namespaceNobody1);

        // create in 11 namespace, make sure there, but not in others
        savedSearches11.create(searchName11, search);
        assertTrue(
            savedSearches11.containsKey(searchName11));
        savedSearches12.refresh();
        assertFalse(
            savedSearches12.containsKey(searchName11));
        savedSearches21.refresh();
        assertFalse(
            savedSearches21.containsKey(searchName11));
        savedSearches22.refresh();
        assertFalse(
            savedSearches22.containsKey(searchName11));

        // create in 12 namespace, make sure there, but not in others
        savedSearches12.create(searchName12, search);
        assertTrue(
            savedSearches12.containsKey(searchName12));
        savedSearches11.refresh();
        assertFalse(
            savedSearches11.containsKey(searchName12));
        savedSearches12.refresh();
        assertFalse(
            savedSearches21.containsKey(searchName12));
        savedSearches22.refresh();
        assertFalse(
            savedSearches22.containsKey(searchName12));

        // create in 21 namespace, make sure there, but not in others
        savedSearches21.create(searchName21, search);
        assertTrue(
            savedSearches21.containsKey(searchName21));
        savedSearches11.refresh();
        assertFalse(
            savedSearches11.containsKey(searchName21));
        savedSearches12.refresh();
        assertFalse(
            savedSearches12.containsKey(searchName21));
        savedSearches22.refresh();
        assertFalse(
            savedSearches22.containsKey(searchName21));

        // create in 22 namespace, make sure there, but not in others
        savedSearches22.create(searchName22, search);
        assertTrue(
            savedSearches22.containsKey(searchName22));
        savedSearches11.refresh();
        assertFalse(
            savedSearches11.containsKey(searchName22));
        savedSearches12.refresh();
        assertFalse(
            savedSearches12.containsKey(searchName22));
        savedSearches21.refresh();
        assertFalse(
            savedSearches21.containsKey(searchName22));

        /* now remove the UNIQUE saved searches */
        savedSearches11.remove(searchName11);
        savedSearches12.remove(searchName12);
        savedSearches21.remove(searchName21);
        savedSearches22.remove(searchName22);
        assertFalse(
            savedSearches11.containsKey(searchName11));
        assertFalse(
            savedSearches12.containsKey(searchName12));
        assertFalse(
            savedSearches21.containsKey(searchName21));
        assertFalse(
            savedSearches22.containsKey(searchName22));

        /* create same search name in different namespaces */
        savedSearches11.create("sdk-test-search", search + " | head 1");
        savedSearches21.create("sdk-test-search", search + " | head 2");
        savedSearchesNobody1.create("sdk-test-search", search + " | head 4");
        assertTrue(
            savedSearches11.containsKey("sdk-test-search"));
        assertTrue(
            savedSearches21.containsKey("sdk-test-search"));
        assertTrue(
            savedSearchesNobody1.containsKey("sdk-test-search"));

        // we have created three saved searches with the same name, make sure we
        // can see all three with a wild-carded get.
        savedSearchesx1.refresh();
        assertEquals(3, savedSearchesx1.values().size());

        assertFalse(
            savedSearchesx1.containsKey("sdk-test-search", namespaceBad));
        assertTrue(
            savedSearchesx1.containsKey("sdk-test-search", namespace21));
        assertTrue(
            savedSearchesx1.get("sdk-test-search", namespace21) != null);

        // remove one of the saved searches through a specific namespace path
        savedSearchesx1.remove("sdk-test-search", namespace21);
        savedSearches11.remove("sdk-test-search");
        savedSearchesNobody1.remove("sdk-test-search");
        assertFalse(
            savedSearches11.containsKey("sdk-test-search"));
        savedSearches21.refresh();
        assertFalse(
            savedSearches21.containsKey("sdk-test-search"));
        assertFalse(
            savedSearchesNobody1.containsKey("sdk-test-search"));

        /* cleanup apps */
        apps.refresh();
        if (apps.containsKey(appname1)) {
            apps.remove(appname1);
            clearRestartMessage();
            apps = service.getApplications();
        }
        if (apps.containsKey(appname2)) {
            apps.remove(appname2);
            clearRestartMessage();
            apps = service.getApplications();
        }
        assertFalse(apps.containsKey(appname1));
        assertFalse(apps.containsKey(appname2));

        /* cleanup users */
        users = service.getUsers(); // need to re-establish, because of restart
        if (users.containsKey(username1))
            users.remove(username1);
        if (users.containsKey(username2))
            users.remove(username2);
        assertFalse(users.containsKey(username1));
        assertFalse(users.containsKey(username2));
    }
}
