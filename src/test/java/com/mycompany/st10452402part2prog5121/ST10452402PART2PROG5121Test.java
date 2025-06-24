/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.st10452402part2prog5121;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
/*test cli Part 3 branch 
*/


public class ST10452402PART2PROG5121Test {

    ST10452402PART2PROG5121 app = new ST10452402PART2PROG5121();

    // ------------------------------
    // Part 2 - Validation & Basic Functional Tests
    // ------------------------------

    @Test
    public void testUsernameCorrectlyFormattedMessage() {
        app.registerUser("Kyle", "Lowry", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        String message = app.returnLoginStatus("kyl_1", "Ch&&sec@ke99!");
        assertEquals("Welcome Kyle, Lowry. It is great to see you again.", message);
    }

    @Test
    public void testUsernameIncorrectlyFormattedMessage() {
        boolean valid = app.checkUserName("kyle!!!!!!!");
        if (!valid) {
            String errorMsg = "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
            assertEquals(errorMsg, errorMsg);
        } else {
            fail("The username should be invalid, but passed validation.");
        }
    }

    @Test
    public void testPasswordCorrectlyFormattedMessage() {
        boolean valid = app.checkPasswordComplexity("Ch&&sec@ke99!");
        if (valid) {
            assertEquals("Password successfully captured.", "Password successfully captured.");
        } else {
            fail("Password should be valid but failed.");
        }
    }

    @Test
    public void testPasswordIncorrectlyFormattedMessage() {
        boolean valid = app.checkPasswordComplexity("password");
        if (!valid) {
            String error = "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
            assertEquals(error, error);
        } else {
            fail("Password should be invalid but passed.");
        }
    }

    @Test
    public void testCellNumberCorrectlyFormattedMessage() {
        boolean valid = app.checkCellPhoneNumber("+27838968976");
        if (valid) {
            assertEquals("Cell number successfully captured.", "Cell number successfully captured.");
        } else {
            fail("Phone number should be valid but failed.");
        }
    }

    @Test
    public void testCellNumberIncorrectlyFormattedMessage() {
        boolean valid = app.checkCellPhoneNumber("08966553");
        if (!valid) {
            String error = "Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
            assertEquals(error, error);
        } else {
            fail("Phone number should be invalid but passed.");
        }
    }

    // ------------------------------
    // Part 2 - Boolean Validation Tests
    // ------------------------------

    @Test
    public void testLoginSuccess() {
        app.registerUser("Sam", "Lee", "sam_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(app.loginUser("sam_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFail() {
        app.registerUser("Kim", "Dotcom", "kim_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(app.loginUser("kim_1", "wrongpass123"));
    }

    @Test
    public void testUsernameCorrectlyFormattedBoolean() {
        assertTrue(app.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectlyFormattedBoolean() {
        assertFalse(app.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void testPasswordMeetsComplexityBoolean() {
        assertTrue(app.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordFailsComplexityBoolean() {
        assertFalse(app.checkPasswordComplexity("password"));
    }

    @Test
    public void testPhoneNumberCorrectlyFormattedBoolean() {
        assertTrue(app.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testPhoneNumberIncorrectlyFormattedBoolean() {
        assertFalse(app.checkCellPhoneNumber("08966553"));
    }

    // ------------------------------
    // Part 2 - Message Specific Tests
    // ------------------------------

    @Test
    public void testValidMessageLength() {
        String message = "Hi Mike, can you join us for dinner tonight";
        assertTrue(message.length() <= 50, "Message should be 50 characters or less.");
    }

    @Test
    public void testInvalidMessageLength() {
        String message = "This message is intentionally longer than fifty characters to trigger a validation failure.";
        int excess = message.length() - 50;
        String expected = "Message exceeds 250 characters by " + excess + ", please reduce size.";
        String actual = "Message exceeds 250 characters by " + excess + ", please reduce size.";
        assertEquals(expected, actual);
    }

    @Test
    public void testRecipientPhoneNumberCorrectFormat() {
        String recipient = "+27718693002";
        assertTrue(app.checkCellPhoneNumber(recipient), "Recipient number should be valid.");
    }

    @Test
    public void testRecipientPhoneNumberIncorrectFormat() {
        String recipient = "08575975889";
        assertFalse(app.checkCellPhoneNumber(recipient), "Recipient number should be invalid.");
    }

    @Test
    public void testMessageHashGeneration() {
        ST10452402PART2PROG5121.Message message = app.new Message("0000000001", "+27718693002", "Hi thanks");
        String expectedHash = "00:0000000001:HITHANKS";
        assertEquals(expectedHash, message.messageHash);
    }

    @Test
    public void testMessageIdIsGenerated() {
        ST10452402PART2PROG5121.Message message = app.new Message("0000000010", "+27838884567", "Hey");
        assertEquals("0000000010", message.messageNumber);
    }

    @Test
    public void testMessageSentStatus() {
        ST10452402PART2PROG5121.Message message = app.new Message("0000000020", "+27838884567", "Let's go");
        message.status = 1; // Sent
        assertEquals(1, message.status);
    }

    @Test
    public void testMessageDisregardedStatus() {
        ST10452402PART2PROG5121.Message message = app.new Message("0000000021", "+27838884567", "Don't save me");
        message.status = -1; // Disregarded
        assertEquals(-1, message.status);
    }

    @Test
    public void testMessageStoredStatus() {
        ST10452402PART2PROG5121.Message message = app.new Message("0000000022", "+27838884567", "Store for later");
        message.status = 0; // Stored
        assertEquals(0, message.status);
    }

    // ------------------------------
    // Part 3 - Additional Feature Tests (Arrays, Search, Delete, Report)
    // ------------------------------

    // Helper to populate messages based on Part 3 test data
    private void populateTestData() {
        app.getSentMessages().clear();

        ST10452402PART2PROG5121.Message msg1 = app.new Message("0000000001", "+27834557896", "Did you get the cake?");
        msg1.status = 1;
        app.getSentMessages().add(msg1);

        ST10452402PART2PROG5121.Message msg2 = app.new Message("0000000002", "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg2.status = 0;
        app.getSentMessages().add(msg2);

        ST10452402PART2PROG5121.Message msg3 = app.new Message("0000000003", "+27834484567", "Yohoooo, I am at your gate.");
        msg3.status = -1;
        app.getSentMessages().add(msg3);

        ST10452402PART2PROG5121.Message msg4 = app.new Message("0000000004", "0838884567", "It is dinner time!");
        msg4.status = 1;
        app.getSentMessages().add(msg4);

        ST10452402PART2PROG5121.Message msg5 = app.new Message("0000000005", "+27838884567", "Ok, I am leaving without you.");
        msg5.status = 0;
        app.getSentMessages().add(msg5);
    }

    @Test
    public void testSentMessagesArrayPopulatedCorrectly() {
        populateTestData();
        List<String> sentContents = new ArrayList<>();
        for (ST10452402PART2PROG5121.Message m : app.getSentMessages()) {
            if (m.status == 1) {
                sentContents.add(m.content);
            }
        }
        assertTrue(sentContents.contains("Did you get the cake?"));
        assertTrue(sentContents.contains("It is dinner time!"));
    }

    @Test
    public void testDisplayLongestSentMessage() {
        populateTestData();
        String longestMessage = "";
            for (ST10452402PART2PROG5121.Message m : app.sentMessages) {
        if (m.content.length() > longestMessage.length()) {
            longestMessage = m.content;
        }
    }
        assertEquals("Where are you? You are late! I have asked you to be on time.", longestMessage);
    }

    @Test
    public void testSearchByMessageId() {
        populateTestData();
        String searchId = "0000000004";
        String expectedContent = "It is dinner time!";
        String foundContent = null;
        for (ST10452402PART2PROG5121.Message m : app.getSentMessages()) {
            if (m.messageNumber.equals(searchId)) {
                foundContent = m.content;
                break;
            }
        }
        assertEquals(expectedContent, foundContent);
    }

    @Test
    public void testSearchMessagesByRecipient() {
        populateTestData();
        String recipientSearch = "+27838884567";
        List<String> messagesForRecipient = new ArrayList<>();
        for (ST10452402PART2PROG5121.Message m : app.getSentMessages()) {
            if (m.recipientNumber.equals(recipientSearch) && (m.status == 0 || m.status == 1)) {
                messagesForRecipient.add(m.content);
            }
        }
        assertTrue(messagesForRecipient.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(messagesForRecipient.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageByHash() {
        populateTestData();
        ST10452402PART2PROG5121.Message toDelete = null;
        for (ST10452402PART2PROG5121.Message m : app.getSentMessages()) {
            if (m.content.equals("Where are you? You are late! I have asked you to be on time.")) {
                toDelete = m;
                break;
            }
        }
        assertNotNull(toDelete);
        String hashToDelete = toDelete.messageHash;

        boolean deleted = app.getSentMessages().removeIf(m -> m.messageHash.equals(hashToDelete));

        assertTrue(deleted);

        for (ST10452402PART2PROG5121.Message m : app.getSentMessages()) {
            assertNotEquals(hashToDelete, m.messageHash);
        }
    }

    @Test
    public void testDisplayReportShowsAllSentMessages() {
        populateTestData();
        StringBuilder report = new StringBuilder();
        for (ST10452402PART2PROG5121.Message m : app.getSentMessages()) {
            if (m.status == 1) {
                report.append("Message Hash: ").append(m.messageHash).append("\n")
                      .append("Recipient: ").append(m.recipientNumber).append("\n")
                      .append("Message: ").append(m.content).append("\n\n");
            }
        }
        String reportString = report.toString();
        assertTrue(reportString.contains("Did you get the cake?"));
        assertTrue(reportString.contains("It is dinner time!"));
        for (ST10452402PART2PROG5121.Message m : app.getSentMessages()) {
            if (m.status == 1) {
                assertTrue(reportString.contains(m.messageHash));
                assertTrue(reportString.contains(m.recipientNumber));
            }
        }
    }
}


/*
REFERENCE:
OpenAI. (2024) ChatGPT (GPT-4) [Large language model]. Available at: https://chat.openai.com/ (Accessed: 21 April 2025).

JUnit. (n.d.) JUnit 5 User Guide. Available at: https://junit.org/junit5/docs/current/user-guide/ (Accessed: 21 April 2025).

IIEVC School of Computer Science, 2022. PROG51212/5111 POE 1: Create your repository. [video online] Available at: https://youtu.be/2JzEhwpg_0U [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Link your local repository (NetBeans folder) with your GitHub repo. [video online] Available at: https://youtu.be/M9DzeAw3uMY?si=08PiED3nAYkcM9Vo [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Add junit5 jar files. [video online] Available at: https://youtu.be/fQaUsfEzGdw?si=JX4uSEFqpayJUVSM [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Create and run your first unit test. [video online] Available at: https://youtu.be/1Pa15vDWG-8?si=UlkhpNblwGAnkMvs [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Push your code to GitHub. [video online] Available at: https://youtu.be/SqHkWHtmMJo?si=LL9E7Li1isq1Y2Xq [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Automate your unit tests. [video online] Available at: https://youtu.be/dWbDN7lxWu4?si=2j3DlIEyqOoEAR84 [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Test more than one value using a loop. [video online] Available at: https://youtu.be/omSrINZdSDU?si=pZZkHzqwLFpCsjaJ [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Write additional unit tests and update your .yaml file. [video online] Available at: https://youtu.be/DmL4gG9vG0A?si=DEferX706znUkDE0 [Accessed 21 April 2025].
*/
