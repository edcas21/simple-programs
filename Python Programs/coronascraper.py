import requests
from bs4 import BeautifulSoup
import pandas as pd
import numpy as np
import smtplib, ssl
import os

EMAIL_ADDRESS = os.environ.get('SENDER_EMAIL')
EMAIL_PASS = os.environ.get('SENDER_PASS')

# send email with stats
def send_mail(country_stats, world_stats):

    country = country_stats['Country'].values[0]
    total_cases = country_stats['Total Cases'].values[0]
    new_cases = country_stats['New Cases'].values[0]
    total_deaths = country_stats['Total Deaths'].values[0]
    new_deaths = country_stats['New Deaths'].values[0]
    total_recovered = country_stats['Total Recovered'].values[0]
    active_cases = country_stats['Active Cases'].values[0]
    serious_critical = country_stats['Serious Critical'].values[0]

    world_tot_cases = world_stats['Total Cases'].values[0]
    world_tot_deaths = world_stats['Total Deaths'].values[0]
    world_tot_recovered = world_stats['Total Recovered'].values[0]


    subject = 'Coronavirus stats in your country today!'
    body = 'World stats:\
        \nTotal cases: ' + world_tot_cases + '\
        \nTotal deaths: ' + world_tot_deaths +'\
        \nTotal recovered: ' + world_tot_recovered + '\
        \n\nToday in ' + country + '\
        \nThere is new data on the coronavirus:\
        \nTotal cases: ' + total_cases + '\
        \nNew Cases: ' + new_cases + '\
        \nTotal Deaths: ' + total_deaths + '\
        \nNew Deaths: ' + new_deaths + '\
        \nActive Cases: ' + active_cases + '\
        \nTotal Recovered ' + total_recovered + '\
        \nSerious, critical cases: ' + serious_critical + '\
        \n\nCheck the link: https://www.worldometers.info/coronavirus/'

    rec_emails = []
    
    # generate an encrypted link
    context = ssl.create_default_context()

    # set up smtp server
    with smtplib.SMTP('smtp.gmail.com', 587) as smtp:

        smtp.ehlo()
        # establish connection with encryption
        smtp.starttls(context=context)
        smtp.ehlo()

        # login to sender email
        smtp.login(EMAIL_ADDRESS, EMAIL_PASS)

        msg = f'Subject: {subject}\n\n{body}'

        # send email
        # for email in rec_emails:
            # smtp.sendmail(EMAIL_ADDRESS, email, msg)
        
        smtp.sendmail(EMAIL_ADDRESS, EMAIL_ADDRESS, msg)


# extract info from dataframe so that it can be sent by email
def info_send(dataframe, country):
    
    # get country stats
    country_stats = dataframe.loc[dataframe['Country'] == country]
    world_stats = dataframe.loc[dataframe['Country'] == 'Total:']

    send_mail(country_stats, world_stats)

def get_info():
    url = "https://www.worldometers.info/coronavirus/"
    results = requests.get(url)

    page = BeautifulSoup(results.text, "html.parser")

    # Set up storage
    countries = []
    total_cases = []
    new_cases = []
    total_deaths = []
    new_deaths = []
    total_recovered = []
    active_cases = []
    serious_critical = []
    total_cases_per_mil = []

    trows = page.table.tbody.find_all('tr')
    total_row = page.find('tr', class_='total_row')
    trows.append(total_row)

    all_data = []

    for row in trows:
        rdata = row.find_all('td')
        country_data = []
        for i in range(len(rdata)):

            data = rdata[i].text.strip()

            if i == 0:
                countries.append(data)
            if i == 1:
                total_cases.append(data)
            if i == 2:
                new_cases.append(data)
            if i == 3:
                total_deaths.append(data)
            if i == 4:
                new_deaths.append(data)
            if i == 5:
                total_recovered.append(data)
            if i == 6:
                active_cases.append(data)
            if i == 7:
                serious_critical.append(data)
            if i == 8:
                total_cases_per_mil.append(data)

    corona_dataframe = pd.DataFrame(
        {
            'Country': countries,
            'Total Cases': total_cases,
            'New Cases': new_cases,
            'Total Deaths': total_deaths,
            'New Deaths': new_deaths,
            'Total Recovered': total_recovered,
            'Active Cases': active_cases,
            'Serious Critical': serious_critical,
            'Tot Cases/1M pop': total_cases_per_mil
        }
    )

    return corona_dataframe

# Main

info_send(get_info(), 'USA')