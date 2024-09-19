package com.parsermoney.parsermoney;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

    private final Repo repo;

    private Document getPage() throws IOException {
        String url = "https://www.mig.kz/";
        try {
            return Jsoup.parse(new URL(url), 3000);
        } catch (IOException e) {
            System.err.println("Failed to fetch the URL: " + e.getMessage());
            throw e;
        }
    }

    public void parseAndSaveData() throws Exception {
        Document page = getPage();

        Elements rows = page.select("div.informer table tbody tr");


        for (Element row : rows) {
            System.out.println("Row HTML: " + row.html());
            String buyPrice = row.select("td[class*=buy]").text();
            String typeMoney = row.select("td.currency").text();
            String salePrice = row.select("td[class*=sell]").text();

            if (!typeMoney.isEmpty() && !buyPrice.isEmpty() && !salePrice.isEmpty()) {
                Money money = new Money();
                money.setBuyPrice(buyPrice);
                money.setTypeMoney(typeMoney);
                money.setSalePrice(salePrice);
                money.setLocalDateTime(LocalDateTime.now());
                repo.save(money);
            } else {
                System.out.println("Skipping row due to empty fields.");
            }
        }
    }

//    private Document getPage() throws IOException {
//        String url = "https://www.mig.kz/";
//        return Jsoup.parse(new URL(url), 3000);
//    }

    public void parseAndUpdateData() throws Exception {
        Document page = getPage();

        Elements rows = page.select("div.informer table tbody tr");

        for (Element row : rows) {
            System.out.println("Row HTML: " + row.html());
            String buyPrice = row.select("td[class*=buy]").text();
            String typeMoney = row.select("td.currency").text();
            String salePrice = row.select("td[class*=sell]").text();

            if (!typeMoney.isEmpty() && !buyPrice.isEmpty() && !salePrice.isEmpty()) {
                Optional<Money> existingMoney = repo.findByTypeMoney(typeMoney);

                Money money;
                if (existingMoney.isPresent()) {
                    money = existingMoney.get();
                    money.setBuyPrice(buyPrice);
                    money.setSalePrice(salePrice);
                    money.setLocalDateTime(LocalDateTime.now());
                } else {
                    money = new Money();
                    money.setBuyPrice(buyPrice);
                    money.setTypeMoney(typeMoney);
                    money.setSalePrice(salePrice);
                    money.setLocalDateTime(LocalDateTime.now());
                }

                repo.save(money);
            } else {
                System.out.println("Skipping row due to empty fields.");
            }
        }
    }
}


