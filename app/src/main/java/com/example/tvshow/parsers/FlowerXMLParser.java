package com.example.tvshow.parsers;

import com.example.tvshow.model.Flower;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bakr- on 9/17/2016.
 */
public class FlowerXMLParser {
    public static List<Flower > parserFeed(String content) {

        try{

            boolean inDataItemTag = false;
            String currentTagName = "";
            Flower flower = null;
            List<Flower> flowerList = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals("product")) {
                            inDataItemTag = true;
                            flower = new Flower();
                            flowerList.add(flower);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("product")) {
                            inDataItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        if (inDataItemTag && flower != null) {
                            switch (currentTagName) {
                                case "productId":
                                    flower.setProductId(Integer.parseInt(parser.getText()));
                                    break;

                                case "name":
                                    flower.setName(parser.getText());
                                break;

                                case "instructions":
                                    flower.setInstructions(parser.getText());
                                break;

                                case "category":
                                    flower.setCategory(parser.getText());
                                break;

                                case "price":
                                    flower.setPrice(Double.parseDouble(parser.getText()));
                                    break;

                                case "photo":
                                    flower.setPhoto(parser.getText());
                                    break;

                                default:
                                    break;
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
            return flowerList;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
