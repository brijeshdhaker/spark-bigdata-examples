#
#
#

from uuid import uuid4
from datetime import datetime
import pytz
import random
#
class User(object):

    # Class Variable
    CITIES = {
        "IN":["Delhi", "Chennei", "Pune", "Mumbai", "Banglore"],
        "USA": ["New York", "Los Angeles", "Miami"],
        "UK": ["London", "Manchester", "Liverpool", "Oxford"],
        "JP": ["Tokyo", "Osaka", "Yokohama", "Hiroshima"]
    }
    COUNTRIES = ["IN", "USA", "UK", "JP"]
    ROLES = ['Admin', 'Technology']
    NAMES = [
        ("Brijesh K", "brijeshdhaker@gmail.com"),
        ("Neeta K", "neetadhk@gmail.com"),
        ("Keshvi K", "keshvid@gmail.com"),
        ("Tejas K", "tejasd@gmail.com")
    ]

    # Use __slots__ to explicitly declare all data members.
    __slots__ = ["id", "uuid", "name", "emailAddr", "age",  "dob", "height", "roles", "status", "addTs", "updTs"]

    # The init method or constructor
    def __init__(self, uuid=None):
        event_datetime = datetime.now(pytz.utc)
        ist_event_datetime = datetime.now(pytz.timezone('Asia/Kolkata'))
        self.addTs = int(event_datetime.strftime("%s"))
        self.updTs = int(event_datetime.strftime("%s"))
        if uuid is None:
            self.uuid = str(uuid4())
        else:
            self.uuid = uuid

    @staticmethod
    def random():
        u = User()
        u.id = random.randint(1000, 5000)
        r_name = random.choice(User.NAMES)
        u.name = random.choice(r_name[0])
        u.emailAddr = random.choice(r_name[1])
        u.age= random.randint(18, 70)
        u.dob= random.randint(18, 70)
        u.height = round(random.uniform(5.0, 7.0))
        u.roles = ['admin', 'Technology'],
        u.status = 'Active'
        return u

    @staticmethod
    def dict_to_name(obj):
        return User(obj['id'])

    @staticmethod
    def name_to_dict(id):
        return User.to_dict(id)

    def to_dict(self):
        return dict(
            id=self.id,
            uuid=self.uuid,
            name=self.name,
            emailAddr= self.emailAddr,
            age= self.age,
            dob=self.dob,
            height=self.height,
            roles=self.roles,
            status=self.status
        )

