package is.idega.idegaweb.pheidippides.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.FinderException;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.MessageFormat;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.text.Name;

import is.idega.idegaweb.pheidippides.business.Currency;
import is.idega.idegaweb.pheidippides.business.RegistrationAnswerHolder;
import is.idega.idegaweb.pheidippides.business.ShirtSizeGender;
import is.idega.idegaweb.pheidippides.business.ShirtSizeSizes;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.Registration;

@Service("pheidippidesUtil")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class PheidippidesUtil {

    @Autowired
    private PheidippidesDao dao;

    public static String concat(String arg1, String arg2) {
        return arg1.concat(arg2);
    }

    public static List<ShirtSizeSizes> getSizes() {
        return Arrays.asList(ShirtSizeSizes.values());
    }

    public static List<ShirtSizeGender> getGenders() {
        return Arrays.asList(ShirtSizeGender.values());
    }

    public static List<Currency> getCurrencies() {
        return Arrays.asList(Currency.values());
    }

    public static String formatDate(Date date, String pattern) {
        if (date != null) {
            return new IWTimestamp(date).getDateString(pattern);
        }
        return "";
    }

    public static String getFullName(String firstName, String middleName, String lastName, Locale locale) {
        return new Name(firstName, middleName, lastName).getName(locale, true);
    }

    public static String escapeXML(String string) {
        string = StringEscapeUtils.unescapeHtml(string.replaceAll("\\<[^>]*>", ""));
        return string;
    }

    public static boolean contains(List<?> objects, Object object) {
        boolean contains = objects.contains(object);
        return contains;
    }

    public static String formatReceipt(String string, RegistrationAnswerHolder answer) {
        String[] args = { String.valueOf(answer.getAmount()), answer.getBankReference().getReferenceNumber() };
        return MessageFormat.format(string, args);
    }

    public static boolean isEnglishLocale(Locale locale) {
        return locale.equals(Locale.ENGLISH);
    }

    public static boolean isOpenForRegistration(Race race) {
        IWTimestamp rightNow = IWTimestamp.RightNow();
        IWTimestamp registrationClose = new IWTimestamp(race.getCloseRegistrationDate());

        return rightNow.isEarlierThan(registrationClose);
    }

    public static boolean registrantHasPersonalId(Registration registration) {
        String userUUID = registration.getUserUUID();

        try {
            UserBusiness ub = (UserBusiness) IBOLookup.getServiceInstance(
                    IWMainApplication.getDefaultIWApplicationContext(),
                    UserBusiness.class);
            User user = ub.getUserByUniqueId(userUUID);

            if (user.getPersonalID() == null || "".equals(user.getPersonalID().trim())) {
                return false;
            }

            return true;
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FinderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return false;
    }

    public static List<AdvancedProperty> getCounterList(int from, int to) {
        List<AdvancedProperty> list = new ArrayList<AdvancedProperty>();

        for (int a = from; a <= to; a++) {
            list.add(new AdvancedProperty(String.valueOf(a), String.valueOf(a)));
        }

        return list;
    }

    public static String getDefaultComment() {
        StringBuilder builder = new StringBuilder();

        int from = 1;
        int to = 5;

        for (int a = from; a <= to; a++) {
            builder.append(a).append(". ");
            if (a < to) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    public PheidippidesDao getDao() {
        return dao;
    }

    public void setDao(PheidippidesDao dao) {
        this.dao = dao;
    }
}