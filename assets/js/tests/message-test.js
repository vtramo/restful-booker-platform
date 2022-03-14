import React from 'react';
import Message from '../src/js/components/Message.jsx';
import nock from 'nock';

beforeAll(() => {
    nock('http://localhost')
        .persist()
        .get('/message/1')
        .reply(200, {
                "name" : "Mark Winteringham",
                "email" : "mark@mwtestconsultancy.co.uk",
                "phone" : "01821 912812",
                "subject" : "Subject description here",
                "description" : "Lorem ipsum dolores est"
            }
        );

    nock('http://localhost')
        .persist()
        .put('/message/1/read')
        .reply(200);
});

test('Message popup is populated with details', async () => {
    const messageComponent = shallow(<Message messageId={1} />);

    setTimeout(() => {
        expect(messageComponent).toMatchSnapshot();
    }, 1000)
});