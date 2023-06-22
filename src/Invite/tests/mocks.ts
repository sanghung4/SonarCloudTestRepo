import { Approver, Role } from 'generated/graphql';
import { InviteUserFormInput } from 'Invite';

export const mockInvite: Omit<InviteUserFormInput, 'erp'> = {
  email: 'farmer.macjoy@yahoo.com',
  firstName: 'Farmer',
  lastName: 'MacJoy',
  userRoleId: 'testfarmer'
};

export const mockRoles: Role[] = [
  {
    id: '2419358e-5c74-4d07-a8d2-37c324afbe6c',
    name: 'Standard Access',
    description: '',
    __typename: 'Role'
  },
  {
    id: 'c1ab78f0-d877-4fe8-bfef-99586d8e4131',
    name: 'Account Admin',
    description: '',
    __typename: 'Role'
  },
  {
    id: 'fc10ca28-33e1-4748-ba3f-45655bbf1fdd',
    name: 'Purchase with Approval',
    description: 'Purchases require Admin approval',
    __typename: 'Role'
  },
  {
    id: 'dd080d8d-b537-4531-bdab-f23268b39e12',
    name: 'Purchase - No Invoices',
    description: 'Invoices hidden for user',
    __typename: 'Role'
  },
  {
    id: '6eb23da7-dc73-49e8-803a-791fa06c24d9',
    name: 'Invoice Only',
    description: '',
    __typename: 'Role'
  }
];

export const mockApprovers: Approver[] = [
  {
    id: '2f658fa2-c34c-492d-ad27-8feec767b851',
    firstName: 'reece',
    lastName: 'qa',
    email: 'reeceqa+uat-admin200010@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '1f26a98f-bf7c-4d58-a818-4702d23a49c6',
    firstName: 'Erik',
    lastName: 'Gabrielsen',
    email: 'erik.gabe+uat01@gmail.com',
    __typename: 'Approver'
  },
  {
    id: 'fc39ef9a-3a2f-4866-b70c-ce5ed4a66d2b',
    firstName: 'Erik',
    lastName: 'Gabrielsen',
    email: 'erik.gabe+uat02@gmail.com',
    __typename: 'Approver'
  },
  {
    id: 'becce1c3-4ef3-4024-acea-16694489fc8a',
    firstName: 'User',
    lastName: 'Test',
    email: 'testingwwuat@gmail.com',
    __typename: 'Approver'
  },
  {
    id: 'f26080d4-a808-40ff-b1cc-a71398ce656e',
    firstName: 'Reece',
    lastName: 'Test',
    email: 'reeceqa+uat-200010-22@gmail.com',
    __typename: 'Approver'
  },
  {
    id: 'c9b79077-494c-4479-8af9-1e73efca9d13',
    firstName: 'Prachi',
    lastName: 'Tundalwar',
    email: 'psttestqa+mincron200010-account@gmail.com',
    __typename: 'Approver'
  },
  {
    id: 'a3ea1c15-ff9b-4606-94c3-e87f06692598',
    firstName: 'Test FirstpkZ',
    lastName: 'Test Laste0B',
    email: 'maxecommercetest+2ikb6ra6u@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '43047906-ff54-4d57-8409-526a3ed611bd',
    firstName: 'Tina',
    lastName: 'Yu',
    email: 'tinayu1888@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '5eace46a-3460-428f-ad79-19c817f1b187',
    firstName: 'test',
    lastName: 'Safari',
    email: 'reeceqa+wwuat+20220914+2@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '216551f5-224a-44dc-9448-b072e4b73453',
    firstName: 'Prachi',
    lastName: 'Tundalwar',
    email: 'psttestqa+uat-admin-200010@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '01217d70-1051-4549-a59f-244faab6ed4b',
    firstName: 'Test First',
    lastName: 'Test Last',
    email: 'maxecommercetest+rnqu4kxw6@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '631386ac-0466-4b9b-bdec-005ce72494c1',
    firstName: 'Prachi',
    lastName: 'Tundalwar',
    email: 'psttestqa+max-fortilinetest1-20010@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '108067cf-5129-44e2-a2a5-21a9bf6b2a2e',
    firstName: 'TestRegression',
    lastName: 'Test10',
    email: 'regressionuattest@gmail.com',
    __typename: 'Approver'
  },
  {
    id: 'edb3867e-72d7-43da-b241-3cf245494c39',
    firstName: 'Prachi',
    lastName: 'Tundalwar',
    email: 'psttestqa+uat-20010@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '34150239-4222-42c2-b604-6d16952d1531',
    firstName: 'Maria',
    lastName: 'Ali',
    email: 'mariaali1906+test3@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '79940e6e-eb65-443c-9fdb-35fdccb3050d',
    firstName: 'test4',
    lastName: 'Ali',
    email: 'mariaali1906+test4@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '50e71e82-268f-47ca-9b5c-8beeeb4e0cf7',
    firstName: 'Zach',
    lastName: 'Schermer',
    email: 'bsagoalie1@aol.com',
    __typename: 'Approver'
  },
  {
    id: '62c1815e-368b-4274-a289-95a770a1038c',
    firstName: 'test',
    lastName: 'edge',
    email: 'reeceqa+0920+edge+2@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '3c6264ff-601b-4bf7-a719-67721f1cc7b9',
    firstName: 'Tina',
    lastName: 'Yu',
    email: 'tinalienyu@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '9ab7377e-2195-4baa-956b-302883fb8423',
    firstName: 'Prachi',
    lastName: 'Tundalwar',
    email: 'psttestqa+uat99-20010@gmail.com',
    __typename: 'Approver'
  },
  {
    id: 'f99bfc85-1135-40a2-9666-16667990103c',
    firstName: 'test',
    lastName: 'test',
    email: 'logan.m.romero+uatadmin200010@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '45b3758b-c33d-4d30-953c-581d2dfacd97',
    firstName: 'Test',
    lastName: 'Mobile',
    email: 'reeceqa+dhtest13124312321@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '5072ee2a-72cf-4afe-8112-675b1b8fd029',
    firstName: 'Prachi',
    lastName: 'Tundalwar-1',
    email: 'psttestqa+uat-reg1-200010@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '5933c968-1a20-4c5a-9a3a-6f2dc52a3f49',
    firstName: 'Safari2',
    lastName: 'Test2',
    email: 'reeceqa+safari+0920+7@gmail.com',
    __typename: 'Approver'
  },
  {
    id: 'c8e4d204-a3a3-484e-b8d8-d44a71873f72',
    firstName: 'Prachi',
    lastName: 'Tundalwar',
    email: 'psttestqa+uatretesrreg1-200010@gmail.com',
    __typename: 'Approver'
  },
  {
    id: 'bb76807b-3109-4835-b657-cf36452eac7c',
    firstName: 'Prachi',
    lastName: 'Tundalwar',
    email: 'psttestqa+uat123450@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '303d2275-9450-497d-84e2-ad44462b6a77',
    firstName: 'Test First8Np',
    lastName: 'Test LastaEp',
    email: 'maxecommercetest+4flpeyojf@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '4bb4903c-824d-492e-b8c6-d9ef51182207',
    firstName: 'etgetereer',
    lastName: 'ddsdssdds',
    email: 'morscoqa12+test2121@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '245793c4-b78e-49cd-8721-7ea560d5b55a',
    firstName: 'Test FirstmJ3',
    lastName: 'Test Lastcme',
    email: 'maxecommercetest+hg9intw5c@gmail.com',
    __typename: 'Approver'
  },
  {
    id: 'a679c7ca-0a07-4d03-89bf-4cbedea72296',
    firstName: 'D',
    lastName: 'Hoff',
    email: 'david.hoffmann+567@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '2a9598ce-a9e4-4f9e-92f9-7a957c81278c',
    firstName: 'test',
    lastName: 'firefox',
    email: 'reeceqa+iudfgouihf@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '3716d043-eaba-4393-8b0b-7bfbeb00aa72',
    firstName: 'Prachi 2',
    lastName: 'Tundalwar',
    email: 'psttestqa+uat-admin_200010@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '7c5245c9-c62c-45ad-be5e-61f1643818e3',
    firstName: 'Prachi',
    lastName: 'Tundalwar',
    email: 'psttestqa+20010-test111@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '94b3054a-7aeb-4002-a867-69aa99eab516',
    firstName: 'Test FirstEcD',
    lastName: 'Test LastAqK',
    email: 'maxecommercetest+xb45fbx7w@gmail.com',
    __typename: 'Approver'
  },
  {
    id: 'f841586c-3bbb-4bb3-beef-d2700ba13b42',
    firstName: 'Test FirstjYj',
    lastName: 'Test Lastu2h',
    email: 'maxecommercetest+a6htc7sko@gmail.com',
    __typename: 'Approver'
  },
  {
    id: '97bc62c4-43d6-4cbe-b934-51fde41f60a2',
    firstName: 'Test FirstnIF',
    lastName: 'Test LastSFk',
    email: 'maxecommercetest+uh5ovv1vd@gmail.com',
    __typename: 'Approver'
  }
];
