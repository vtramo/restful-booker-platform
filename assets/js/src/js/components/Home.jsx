import React from 'react';
import HotelRoomInfo from './HotelRoomInfo.jsx';
import HotelMap from './HotelMap.jsx';
import HotelLogo from './HotelLogo.jsx';
import HotelContact from './HotelContact.jsx';

import adbanner from '../../images/ait-banner.png';

import { API } from '../libs/Api.js';

export default class Home extends React.Component {

    constructor() {
        super();

        this.state = {
            rooms : [],
            branding : {}
        }
    }

    componentDidMount() {
        API.getBranding(this);

        API.getRoom(this);
    }

    render() {
        return (
            <div>
                <div className="container-fluid">
                    <div className="row">
                        <div className="col-12">
                            <a href="https://www.ministryoftesting.com/events/automation-in-testing-28-to-30-march-2022?utm_medium=web&utm_source=aitonline" alt="Link to Automation in Testing training in Edinburgh"><img src={adbanner} /></a>
                        </div>
                    </div>
                    <div className="row">
                        <div className='col-sm-12 text-center'>
                            <HotelLogo logoDetails={this.state.branding.logoUrl} />
                        </div>
                    </div>
                    <div className="row hotel-description">
                        <div className='col-sm-1'></div>
                        <div className='col-sm-10'>
                            <p>{this.state.branding.description}</p>
                        </div>
                        <div className='col-sm-1'></div>
                    </div>
                    <div className="row room-header">
                        <div className='col-sm-1'></div>
                        <div className='col-sm-10'>
                            <h2>Rooms</h2>
                        </div>
                        <div className='col-sm-1'></div>
                    </div>
                    {this.state.rooms.map((roomDetails) => {
                        return <div key={roomDetails.roomid}><HotelRoomInfo room={roomDetails} /></div>
                    })}
                        <HotelContact contact={this.state.branding.contact} />
                    <div className="row">
                        <div className='col-sm-12'>
                            <HotelMap name={this.state.branding.name} mapDetails={this.state.branding.map} />
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
