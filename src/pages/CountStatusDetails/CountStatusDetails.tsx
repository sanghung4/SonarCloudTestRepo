import React, { useEffect } from "react";
import { Banner } from "../../components/Banner";
import { useGetCountStatusQuery } from "../../graphql";
import { Button } from "../../components/Button";
import { useParams } from "react-router-dom";
import { getFormattedDate, getFormattedTime } from "../../utils/dates";
import { Configuration } from "../../utils/configuration";
import { useHistory, useLocation} from "react-router-dom";


const CountStatus = () => {
    // ----------------- //
    // ----- Hooks ----- //
    // ----------------- //
    const { id, total } = useParams<{ id: string, total: string }>();
    const history = useHistory();

    // --------------- //
    // ----- API ----- //
    // --------------- //
    //  GetCountStatus
    const { data, loading, refetch, startPolling, stopPolling } = useGetCountStatusQuery({
        variables: {
            id
        },
        fetchPolicy: 'network-only',
        pollInterval: Number(Configuration.countPollingInterval) * 2,
        onError: (error) => {
            history.goForward()
        }
    });

    // ------------------- //
    // ----- Actions ----- //
    // ------------------- //
    const handleRefresh = () => {
        refetch();
    };

    /**
     * Loading State
     */
    if (loading || !data) {
        return (
            <div className='flex items-center justify-center'>
                <div className='w-full'>
                    <div className='p-5 bg-white rounded shadow-md m-4'>
                        <div className='flex justify-between text-sm 2xl:text-base text-gray-800 '>
                            Loading...
                        </div>
                    </div>
                </div>
            </div>
        )
    }

    const { countStatus } = data;

    if (countStatus.totalProducts && countStatus.totalProducts > 0) {
        startPolling(Number(Configuration.countPollingInterval))
    }

    if (countStatus.status !== 'IN_PROGRESS') {
        stopPolling();
    }

    const getStatusText = (status: string) => {
        if (status === 'COMPLETE') {
            return 'You\'re all set!';
        } else if (status === 'IN_PROGRESS') {
            return 'Loading is in progress...'
        } else if (status === 'NOT_LOADED') {
            return 'Not Started'
        } else {
            return 'Error'
        }
    }

    const getStatusDetailsText = (status: string) => {
        if (status === 'COMPLETE') {
            return 'Your count has been loaded and is now ready to be used in the BIA mobile app';
        } else if (status === 'IN_PROGRESS') {
            return 'This may take a few minutes. Please click "refresh status" to get an updated status'
        } else if (status === 'NOT_LOADED') {
            return 'Your count has not been loaded into the BIA mobile app. Please load this count by visiting the counts dashboard'
        } else {
            return 'An error occurred while loading your count. Please contact an admin for assistance'
        }
    }

    const adjustCount = countStatus.branchId.length > 3 ? 1 : 0;
    const totalExpected = (Number(total) > 0) ? Number(total) - adjustCount : countStatus.totalProducts!;
    const percentageCompleted = ((countStatus.totalProducts! / totalExpected) * 100).toFixed(2);

    return (
        <>
            <Banner header='Count Status Progress' />
            <div className='flex items-center justify-center'>
                <div className='w-full'>
                    <div className='p-5 bg-white rounded shadow-md m-10' style={{ textAlign: 'center' }}>
                        <div className='my-2 mx-5 md:mx-8 text-reece-800 font-semibold text-2xl'>
                            Count ID: {countStatus.countId}
                        </div>
                        <div className='my-2 mx-5 md:mx-8 text-reece-800 text-xl'>
                            Branch {countStatus.branchId} - {countStatus.branchName}
                        </div>
                        <div className='my-2 mx-5 md:mx-8 text-reece-1000 text-md'>
                            created @ {getFormattedDate(countStatus.createdAt ?? '')} {getFormattedTime(countStatus.createdAt ?? '')}
                        </div>
                        <br />
                        <h1 className='text-reece-900 text-4xl pb-2 font-semibold'>{getStatusText(countStatus.status ?? '')}</h1>
                        <h1 className='text-reece-1000 text-l font-semibold'>{getStatusDetailsText(countStatus.status ?? '')}</h1>
                        <h1 className='text-reece-900 text-2xl pt-2 font-semibold'>{percentageCompleted}%</h1>
                        <p className='mb-2 mx-5 md:mx-8 text-reece-1000 text-md'>{countStatus.totalProducts} / {totalExpected} products</p>
                        {countStatus.status !== 'COMPLETE' && (
                            <Button onClick={handleRefresh} title="Refresh Status" className='mt-5 bg-reece-500 hover:bg-reece-600 text-red-50 text-l' />
                        )}
                    </div>
                </div>
            </div>
        </>
    );
};

export default CountStatus;
