/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.demo.iteration.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.richfaces.demo.iteration.model.Company;
import org.richfaces.demo.iteration.model.Employee;


public final class EmployeeUtils {
    private EmployeeUtils() {
    }

    public static Collection<Employee> obtainDefaultEmployeeList() {

        Collection<Employee> employeeList = new LinkedHashSet<Employee>();

        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");
        addEmployeeToCollection(employeeList, "Randy Davenport", "Database Administrator", "rdevenport@savatrip.com",
                "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
        addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen", "Computer Specialist", "kakohlscheen@eni.com",
                "Eni S.p.A.", "+031-455223300", "Andria, Bari Italy");
        addEmployeeToCollection(employeeList, "M Koetsier", "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                "316-838-4663", "Moscow,  Russia");
        addEmployeeToCollection(employeeList, "James Kneale", "General Manager", "jkneale@paleo.com", "Paleo Inc",
                "+91-484-272-2061", "Hamburg,  Germany");
        addEmployeeToCollection(employeeList, "Andrew Knaebel", "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                "512-927-3549", "Redhill, Surrey England");
        addEmployeeToCollection(employeeList, "Andy Kirkham", "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                "561-642-2153", "Ashton, ID United States");
        addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat", "CTO", "aksinhuat@aes.com", "The AES Corporation",
                "864-233-4064", "Sydney,  Australia");
        addEmployeeToCollection(employeeList, "Rick Kelsven", "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                "208-356-4991", "Bangkok,  Thailand");
        addEmployeeToCollection(employeeList, "9Alexander Ivanov", "Director", "aivanov@beltelecom.by", "AMicrosoft",
                "+375 29 255 00 00", "Minsk, Belarus");
        addEmployeeToCollection(employeeList, "4Bill Gates", "President", "bgates@microsoft.com", "AMicrosoft", "817-335-5881",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Amanda Gellhouse", "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                "853-729-3784", "Sun Valley, USA");
        addEmployeeToCollection(employeeList, "2Hideo Kodzima", "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                "419-615-2730", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "3Stan Carpenter", "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                "714-647-3380", "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Clement Gaudet", "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                "403-444-1100", "Canada");
        addEmployeeToCollection(employeeList, "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony", "410-561-4400",
                "Tokyo, Japan");
        addEmployeeToCollection(employeeList, "Willis Aberg", "VP and CIO", "waberg@evjscet.com", "Evj Scet", "+046-155248000",
                "Tacoma, WA United States");
        addEmployeeToCollection(employeeList, "Nick Acree", "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                "781-229-9599", "Houston, TX United States");
        addEmployeeToCollection(employeeList, "RIchard L. Adams", "Technology", "rladams@penergy.com", "Panhandle Energy",
                "713-789-1400", "Valencia, CA United States");
        addEmployeeToCollection(employeeList, "Vicki Ahlstrom", "Chief information Officer", "vahlstrom@nassjok.com",
                "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
        addEmployeeToCollection(employeeList, "Candace Alexander", "VP Supply Chain", "acandace@ucr.com",
                "Usgs Central Region", "248-666-3500", "Holyoke, MA United States");
        addEmployeeToCollection(employeeList, "Ronnie Allen", "VP Power Generation Technology", "rallen@oilhouse.com",
                "The Oil House", "540-672-1144", "Hesperia, CA United States");
        addEmployeeToCollection(employeeList, "Philip Krell", "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                "+007-78129533724", "Austin, TX United States");
        addEmployeeToCollection(employeeList, "Michael Johnston", "MIS", "mjohnstom@scotteq.com", "Scott Equipment Co",
                "+031-104600660", "Nashville, TN United States");
        addEmployeeToCollection(employeeList, "Arne Johansson", "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                "248-474-2790", "Scottsdale, AZ United States");
        addEmployeeToCollection(employeeList, "Gary Jenkins", "Senior Vice-President; Technology", "gjenkins@lorien.com",
                "Lorien", "608-788-4000", "Yorba Linda, CA United States");
        addEmployeeToCollection(employeeList, "R.K. Jain", "VP Information Management", "rjlain@broeren.com",
                "Broeren Oil Inc", "812-477-1529", "Martinsville, VA United States");
        addEmployeeToCollection(employeeList, "Kathryn J. Jackson", "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                "+046-480450500", "Norco, LA United States");
        addEmployeeToCollection(employeeList, "Hamid Abbasi", "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                "+043-318224190", "Spartanburg, SC United States");
        addEmployeeToCollection(employeeList, "Stephanie Cobb", "Vice-President; Systems Staff", "scobb@amarillcity.com",
                "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
        addEmployeeToCollection(employeeList, "Titus J Colaco", "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                "337-856-6704", "St. John's, NF Canada");
        addEmployeeToCollection(employeeList, "Steve Coleman", "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                "303-688-5946", "Burke, VA United States");
        addEmployeeToCollection(employeeList, "Chris K Corcoran", "EVP, Oxbow Calcining", "ckcorcoran@buck.com",
                "D S Buck Inc", "+47-23-01-49-70", "Soskut,  Hungary");
        addEmployeeToCollection(employeeList, "Paulo R. Costa", "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                "+46-372789383", "Concord, NH United States");
        addEmployeeToCollection(employeeList, "Daniel Crespo", "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                "Bokken As", "+91-22-6659-7300", "Wien, Austria");
        addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef", "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                "Liberal, KS United States");
        addEmployeeToCollection(employeeList, "James Curley", "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                "+047-52020000", "Nesflaten, Rogaland Norway");
        addEmployeeToCollection(employeeList, "Lu Dam", "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                "313-876-0190", "Walters, OK United States");/*
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              * addEmployeeToCollection(employeeList, "9Alexander Ivanov",
                                                              * "Director", "aivanov@beltelecom.by", "AMicrosoft",
                                                              * "+375 29 255 00 00", "Minsk, Belarus");
                                                              * addEmployeeToCollection(employeeList, "4Bill Gates",
                                                              * "President", "bgates@microsoft.com", "AMicrosoft",
                                                              * "817-335-5881", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Amanda Gellhouse",
                                                              * "IT/Internet Support; Manager", "agellhouse@sun.com", "Sun",
                                                              * "853-729-3784", "Sun Valley, USA");
                                                              * addEmployeeToCollection(employeeList, "2Hideo Kodzima",
                                                              * "Vice Prezident", "khideo@konami.jp", "AMicrosoft",
                                                              * "419-615-2730", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "3Stan Carpenter",
                                                              * "Lead Designer", "scarpenter@ndogs.com", "AMicrosoft",
                                                              * "714-647-3380", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Clement Gaudet",
                                                              * "Chief Technology Officer", "cgaudet@rstar.com", "Rock Star",
                                                              * "403-444-1100", "Canada"); addEmployeeToCollection(employeeList,
                                                              * "Kazunori Yamauchi", "CEO", "ykazunori@sony.jp", "Sony",
                                                              * "410-561-4400", "Tokyo, Japan");
                                                              * addEmployeeToCollection(employeeList, "Willis Aberg",
                                                              * "VP and CIO", "waberg@evjscet.com", "Evj Scet",
                                                              * "+046-155248000", "Tacoma, WA United States");
                                                              * addEmployeeToCollection(employeeList, "Nick Acree",
                                                              * "Computer Technician", "nacree@grifcity.com", "City Of Griffin",
                                                              * "781-229-9599", "Houston, TX United States");
                                                              * addEmployeeToCollection(employeeList, "RIchard L. Adams",
                                                              * "Technology", "rladams@penergy.com", "Panhandle Energy",
                                                              * "713-789-1400", "Valencia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Vicki Ahlstrom",
                                                              * "Chief information Officer", "vahlstrom@nassjok.com",
                                                              * "AMicrosoft", "+420-545218196", "Aurskog, Akershus Norway");
                                                              * addEmployeeToCollection(employeeList, "Candace Alexander",
                                                              * "VP Supply Chain", "acandace@ucr.com", "Usgs Central Region",
                                                              * "248-666-3500", "Holyoke, MA United States");
                                                              * addEmployeeToCollection(employeeList, "Ronnie Allen",
                                                              * "VP Power Generation Technology", "rallen@oilhouse.com",
                                                              * "The Oil House", "540-672-1144", "Hesperia, CA United States");
                                                              * addEmployeeToCollection(employeeList, "Philip Krell",
                                                              * "MIS Manager", "pkrell@mainpartner.com", "Maintpartner Ab",
                                                              * "+007-78129533724", "Austin, TX United States");
                                                              * addEmployeeToCollection(employeeList, "Michael Johnston", "MIS",
                                                              * "mjohnstom@scotteq.com", "Scott Equipment Co", "+031-104600660",
                                                              * "Nashville, TN United States");
                                                              * addEmployeeToCollection(employeeList, "Arne Johansson",
                                                              * "Network Manager", "ajohansson@oneok.com", "Oneok, Inc",
                                                              * "248-474-2790", "Scottsdale, AZ United States");
                                                              * addEmployeeToCollection(employeeList, "Gary Jenkins",
                                                              * "Senior Vice-President; Technology", "gjenkins@lorien.com",
                                                              * "Lorien", "608-788-4000", "Yorba Linda, CA United States");
                                                              * addEmployeeToCollection(employeeList, "R.K. Jain",
                                                              * "VP Information Management", "rjlain@broeren.com",
                                                              * "Broeren Oil Inc", "812-477-1529",
                                                              * "Martinsville, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Kathryn J. Jackson",
                                                              * "MIS Director", "kjjackson@alon.com", "Alon Usa, Lp",
                                                              * "+046-480450500", "Norco, LA United States");
                                                              * addEmployeeToCollection(employeeList, "Hamid Abbasi",
                                                              * "Programmer Analyst", "habbasi@kwb.com", "K W B Inc",
                                                              * "+043-318224190", "Spartanburg, SC United States");
                                                              * addEmployeeToCollection(employeeList, "Stephanie Cobb",
                                                              * "Vice-President; Systems Staff", "scobb@amarillcity.com",
                                                              * "City Of Amarillo", "213-367-4211", "Auburn, IL United States");
                                                              * addEmployeeToCollection(employeeList, "Titus J Colaco",
                                                              * "VP L.T. and CIO", "tjcolaco@gary.com", "Gary Inc",
                                                              * "337-856-6704", "St. John's, NF Canada");
                                                              * addEmployeeToCollection(employeeList, "Steve Coleman",
                                                              * "Webmaster", "scoleman@ragnsells.com", "Ragn-Sells Ab",
                                                              * "303-688-5946", "Burke, VA United States");
                                                              * addEmployeeToCollection(employeeList, "Chris K Corcoran",
                                                              * "EVP, Oxbow Calcining", "ckcorcoran@buck.com", "D S Buck Inc",
                                                              * "+47-23-01-49-70", "Soskut,  Hungary");
                                                              * addEmployeeToCollection(employeeList, "Paulo R. Costa",
                                                              * "VP Power Production", "prcosta@sasol.com", "Sasol Limited",
                                                              * "+46-372789383", "Concord, NH United States");
                                                              * addEmployeeToCollection(employeeList, "Daniel Crespo",
                                                              * "IT/Internet Support; Analyst", "dcrespo@bokken.com",
                                                              * "Bokken As", "+91-22-6659-7300", "Wien, Austria");
                                                              * addEmployeeToCollection(employeeList, "Petr Cumba", "IT-chef",
                                                              * "pcumba@abarta.com", "ABARTA, Inc.", "701-277-0403",
                                                              * "Liberal, KS United States");
                                                              * addEmployeeToCollection(employeeList, "James Curley",
                                                              * "Network Manager", "jcurley@triboro.com", "Triboro Gulf",
                                                              * "+047-52020000", "Nesflaten, Rogaland Norway");
                                                              * addEmployeeToCollection(employeeList, "Lu Dam",
                                                              * "SVP Operations and Technical", "ludam@krepro.com", "Krepro As",
                                                              * "313-876-0190", "Walters, OK United States");
                                                              * addEmployeeToCollection(employeeList, "Randy Davenport",
                                                              * "Database Administrator", "rdevenport@savatrip.com",
                                                              * "Sav-A-Trip, Inc", "+032-38807700", "Adelaide,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Kevin A. Kohlscheen",
                                                              * "Computer Specialist", "kakohlscheen@eni.com", "Eni S.p.A.",
                                                              * "+031-455223300", "Andria, Bari Italy");
                                                              * addEmployeeToCollection(employeeList, "M Koetsier",
                                                              * "Programmer", "mkoetsier@pinnergy.com", "Pinnergy Ltd",
                                                              * "316-838-4663", "Moscow,  Russia");
                                                              * addEmployeeToCollection(employeeList, "James Kneale",
                                                              * "General Manager", "jkneale@paleo.com", "Paleo Inc",
                                                              * "+91-484-272-2061", "Hamburg,  Germany");
                                                              * addEmployeeToCollection(employeeList, "Andrew Knaebel",
                                                              * "Technical Staff", "aknaebel@amoco.com", "Hilltop Amoco",
                                                              * "512-927-3549", "Redhill, Surrey England");
                                                              * addEmployeeToCollection(employeeList, "Andy Kirkham",
                                                              * "VP Administration", "akirkham@pacificorp.com", "Pacificorp",
                                                              * "561-642-2153", "Ashton, ID United States");
                                                              * addEmployeeToCollection(employeeList, "Derrik Khoo Sin Huat",
                                                              * "CTO", "aksinhuat@aes.com", "The AES Corporation",
                                                              * "864-233-4064", "Sydney,  Australia");
                                                              * addEmployeeToCollection(employeeList, "Rick Kelsven",
                                                              * "Network Analyst", "rkelsven@rudny.com", "Rudny's Inc",
                                                              * "208-356-4991", "Bangkok,  Thailand");
                                                              */
        return employeeList;
    }

    private static void addEmployeeToCollection(Collection<Employee> collection, String employeeName, String employeeTitle,
            String employeeEmail, String companyName, String companyPhone, String companyState) {
        Company company1 = new Company(companyName);
        company1.setPhone(companyPhone);
        company1.setState(companyState);

        Company company2 = new Company("stubName1");
        company2.setPhone("stubPhone1");
        company2.setState("stubState1");

        Company company3 = new Company("stubName2");
        company3.setPhone("stubPhone2");
        company3.setState("stubState2");

        List<Company> companies = new ArrayList<Company>();
        companies.add(company1);
        companies.add(company2);
        companies.add(company3);

        Employee employee = new Employee(employeeName, employeeTitle);
        employee.setEMail(employeeEmail);
        employee.setCompanies(companies);
        collection.add(employee);
    }
}
