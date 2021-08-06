package com;

import com.example.myfirstapp.Message;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTests {

    //  test message methods


    @Test
    public void getMsg() {
        Message basicMsg= new Message();
        basicMsg.setMsg("Hello");
        assertEquals("getMsg() did not get the right message","Hello", basicMsg.getMsg());
    }

    @Test
    public void setMsg(){
        Message basicMsg= new Message();
        basicMsg.setMsg("Hello");
        assertEquals("setMsg() did not set the right message","Hello", basicMsg.getMsg());
    }

    @Test
    public void getDateSent(){
        Message basicMsg= new Message();
        basicMsg.setDateSent("12/02/2021");
        assertEquals("getDateSent() did not get the right date","12/02/2021", basicMsg.getDateSent());
    }

    @Test
    public void setDateSent(){
        Message basicMsg= new Message();
        basicMsg.setDateSent("12/02/2021");
        assertEquals("setDateSent() did not set the right date","12/02/2021", basicMsg.getDateSent());

    }

    @Test
    public void getRead_Unread(){
        Message basicMsg= new Message();
        basicMsg.setRead_unread(true);
        assertEquals("getRead_Unread() did not get the right status", true, basicMsg.getRead_unread());
    }

    @Test
    public void setRead_Unread(){
        Message basicMsg= new Message();
        basicMsg.setRead_unread(true);
        assertEquals("setRead_Unread() did not set the right status", true, basicMsg.getRead_unread());

    }
    @Test
    public void getTo(){
        Message basicMsg= new Message();
        basicMsg.setTo("Isaac");
        assertEquals("getTo() did not get the right receiver","Isaac", basicMsg.getTo());
    }

    @Test
    public void setTo(){
        Message basicMsg= new Message();
        basicMsg.setTo("Isaac");
        assertEquals("setTo() did not set the right receiver","Isaac", basicMsg.getTo());
    }

    @Test
    public void getFrom(){
        Message basicMsg= new Message();
        basicMsg.setFrom("Saly");
        assertEquals("getFrom() did not get the right sender","Saly", basicMsg.getFrom());
    }

    @Test
    public void setFrom(){
        Message basicMsg= new Message();
        basicMsg.setFrom("Saly");
        assertEquals("setFrom() did not set the right sender","Saly", basicMsg.getFrom());
    }
}
