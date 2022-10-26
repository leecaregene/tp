package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.meeting.MeetingDate;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Income;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.portfolio.Note;
import seedu.address.model.portfolio.Plan;
import seedu.address.model.portfolio.Portfolio;
import seedu.address.model.portfolio.Risk;
import seedu.address.model.tag.Tag;

/**
 * OpenCSV-friendly version of {@link Person}.
 */
public class CsvAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    @CsvBindByName(required = true)
    private final String name;
    @CsvBindByName(required = true)
    private final String phone;
    @CsvBindByName(required = true)
    private final String email;
    @CsvBindByName(required = true)
    private final String address;
    @CsvBindByName(required = true)
    private final String income;
    @CsvBindByName(column = "meeting date")
    private final String meetingDate;
    @CsvBindAndSplitByName(column = "tags",
            elementType = Tag.class, splitOn = ",", converter = StringToTag.class, writeDelimiter = ",")
    private final List<Tag> tagged = new ArrayList<>();
    @CsvBindByName(column = "risk")
    private final String risk;
    @CsvBindAndSplitByName(column = "plans",
            elementType = Plan.class, splitOn = ",", converter = StringToPlan.class, writeDelimiter = ",")
    private final List<Plan> planned = new ArrayList<>();
    @CsvBindByName(column = "note")
    private final String note;

    /**
     * OpenCSV requires a public nullary constructor
     */
    public CsvAdaptedPerson() {
        this.name = null;
        this.phone = null;
        this.email = null;
        this.address = null;
        this.income = null;
        this.meetingDate = null;
        this.risk = null;
        this.note = null;
    }

    /**
     * Constructs a {@code CsvAdaptedPerson} with the given person details.
     */
    public CsvAdaptedPerson(String name, String phone,
                            String email, String address,
                            String income,
                            String meetingDate,
                            List<Tag> tagged,
                            String risk,
                            List<Plan> planned,
                            String note) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.income = income;
        if (meetingDate != null) {
            this.meetingDate = meetingDate;
        } else {
            this.meetingDate = "";
        }
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
        if (risk != null) {
            this.risk = risk;
        } else {
            this.risk = "";
        }
        if (planned != null) {
            this.planned.addAll(planned);
        }
        if (note != null) {
            this.note = note;
        } else {
            this.note = "";
        }
    }

    /**
     * Converts a given {@code Person} into this class for OpenCSV use.
     */
    public CsvAdaptedPerson(Person source) {
        Portfolio portfolio = source.getPortfolio();
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        income = source.getIncome().value;
        meetingDate = source.getMeetingDate().value;
        tagged.addAll(source.getTags());
        risk = portfolio.getRisk().value;
        planned.addAll(portfolio.getPlans());
        note = portfolio.getNote().value;
    }

    /**
     * Converts this OpenCSV-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (Tag tag : tagged) {
            if (!tag.tagName.equals("null")) {
                personTags.add(tag);
            }
        }

        final List<Plan> personPlans = new ArrayList<>();
        for (Plan plan : planned) {
            if (!plan.value.equals("null")) {
                personPlans.add(plan);
            }
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (income == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Income.class.getSimpleName()));
        }
        if (!Income.isValidIncome(income)) {
            throw new IllegalValueException(Income.MESSAGE_CONSTRAINTS);
        }
        final Income modelIncome = new Income(income);

        if (meetingDate != null && !MeetingDate.isValidMeetingDate(meetingDate)) {
            throw new IllegalValueException(MeetingDate.MESSAGE_CONSTRAINTS);
        }
        final MeetingDate modelMeetingDate;

        if (meetingDate != null) {
            modelMeetingDate = new MeetingDate(meetingDate);
        } else {
            modelMeetingDate = new MeetingDate("");
        }

        if (risk != null && !Risk.isValidRisk(risk)) {
            throw new IllegalValueException(Risk.MESSAGE_CONSTRAINTS);
        }
        final Risk modelRisk;

        if (risk != null) {
            modelRisk = new Risk(risk);
        } else {
            modelRisk = new Risk("");
        }

        if (note != null && !Note.isValidNote(note)) {
            throw new IllegalValueException(Note.MESSAGE_CONSTRAINTS);
        }
        final Note modelNote;

        if (note != null) {
            modelNote = new Note(note);
        } else {
            modelNote = new Note("");
        }

        final Set<Plan> modelPlan = new HashSet<>(personPlans);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelIncome, modelMeetingDate, modelTags,
                modelRisk, modelPlan, modelNote);
    }

}
